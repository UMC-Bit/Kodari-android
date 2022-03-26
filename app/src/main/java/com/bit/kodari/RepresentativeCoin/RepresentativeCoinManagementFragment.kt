package com.bit.kodari.RepresentativeCoin

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Adapter.RptCoinManagementAdapter
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinMgtInsquireView
import com.bit.kodari.RepresentativeCoin.RetrofitData.DeleteRptCoinResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.Util.Coin.*
import com.bit.kodari.Util.Coin.Binance.BinanceWebSocketListener
import com.bit.kodari.Util.Coin.Bithumb.BithumbWebSocketListener
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.bit.kodari.Util.Upbit.CoinService
import com.bit.kodari.databinding.FragmentRepresentativeCoinManagementBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//삭제 누르면 끝나고 작업 다시 재조회 -> deletList 초기화
class RepresentativeCoinManagementFragment(val marketIdx:Int) : BaseFragment<FragmentRepresentativeCoinManagementBinding>(FragmentRepresentativeCoinManagementBinding::inflate), RptCoinMgtInsquireView,
    CoinView {
    var usdtPrice = HomeFragment.usdtPrice
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var coinSymbolSet = HashSet<String>()    // 유저 코인, 대표 코인 심볼 저장
    private val marketName = MyApplicationClass.marketName // 거래소 이름
    var upbitWebSocket: UpbitWebSocketListener? = null    // 업비트 웹 소켓
    var bithumbWebSocket: BithumbWebSocketListener? = null    // 업비트 웹 소켓
    var binanceWebSocket: BinanceWebSocketListener? = null // 바이낸스 웹 소켓
    private var coinList = ArrayList<RepresentCoinResult>()
    private var rptCoinManagementAdapter = RptCoinManagementAdapter(coinList)
    private var deleteRcoinList = HashSet<Int>()      //대표코인 삭제할 리스트들
    private lateinit var callback: OnBackPressedCallback
    private lateinit var coinService: CoinService

    companion object{
        private var cnt = 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, HomeFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        // Usdt 환율 받아옴
        setInit()
        deleteDialog()
        setListener()
        getRptCoins()

        // ViewModel 적용
        viewModelFactory = CoinViewModelFactory(null, coinList)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        // 처음에 코인 시세 불러오기
        coinService = CoinService()
        coinService.setViewModel(viewModel)
        viewModel.representCoinData.observe(this, androidx.lifecycle.Observer {
            if(rptCoinManagementAdapter != null){
                var position = viewModel.getRepresentCoinPosition()
                rptCoinManagementAdapter.setData(coinList, position)
            }else {
                setRecyclerView()
            }
        })
    }

    private fun setInit() { //marketIdx 에 따른 텍스트 뷰 셋팅
        if(marketIdx == 2) {
            binding.representativeCoinManagementUpbitTV.text = "빗썸"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        upbitWebSocket?.webSocket?.cancel() // 웹 소켓 닫기
        bithumbWebSocket?.webSocket?.cancel()
        binanceWebSocket?.webSocket?.cancel()
    }
    fun setListener(){
        binding.representativeCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinSearchFragment(marketIdx)).commit()
        }

        binding.representativeCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commit()
        }
        // 업비트 말고 다른 거래소 일 때 타이틀 변경
        if(MyApplicationClass.marketName.equals("빗썸")){
            binding.representativeCoinManagementUpbitTV.setText("빗썸")
        }
    }

    fun setRecyclerView(){
        val animator = binding.representativeCoinManagementRV.itemAnimator // 애니메이션 제거
        if(animator is SimpleItemAnimator) { //아이템 애니메이커 기본 하위클래스
            animator.supportsChangeAnimations =
                false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
        }
        rptCoinManagementAdapter= RptCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        rptCoinManagementAdapter.setMyItemClickListener(object :
            RptCoinManagementAdapter.MyItemClickListener{
            override fun onItemCheck(item: RepresentCoinResult) {  //선택했을때 -> off 눌렀을때
                item.isChecked = true                                   //true로 변경
                if(!deleteRcoinList.contains(item.representIdx)){
                    deleteRcoinList.add(item.representIdx)
                    Log.d("deleteRCoin", "삭제 리스트 추가 : ${item.representIdx}")
                }
            }

            override fun onItemUnCheck(item: RepresentCoinResult) {    //선택 해제했을때 -> on 눌렀을 때
                item.isChecked = false
                if(deleteRcoinList.contains(item.representIdx)){
                    deleteRcoinList.remove(item.representIdx)
                    Log.d("deleteRCoin", "삭제 리스트 제거 : ${item.representIdx}")
                }
            }
        })
            binding.representativeCoinManagementRV.layoutManager =
                LinearLayoutManager(context as MainActivity)
            binding.representativeCoinManagementRV.adapter = rptCoinManagementAdapter

    }

    //대표 코인 조회
    fun getRptCoins(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinMgtInsquireView(this)
        showLoadingDialog(requireContext())
        rptCoinService.getRptCoinMgtInsquire()
    }

    fun deleteRcoin(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinMgtInsquireView(this)
        showLoadingDialog(requireContext())
        rptCoinService.deleteRptCoin(deleteRcoinList)
    }

    fun deleteDialog()
    {
        binding.representativeCoinManagementDeleteButtonIB.setOnClickListener {
            val deleteAlertDialog = MaterialAlertDialogBuilder(context as MainActivity, R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
                .setView(R.layout.fragment_representative_coin_delete_dialog).show()

            val deleteConfirmButton=deleteAlertDialog.findViewById<TextView>(R.id.representative_coin_delete_dialog_delete_confirm_TV)
            val cancelButton=deleteAlertDialog.findViewById<TextView>(R.id.representative_coin_delete_dialog_cancel_TV)
            val deleteAskTextView = deleteAlertDialog.findViewById<TextView>(R.id.representative_coin_delete_dialog_ask_TV)

            //글자 색 바꾸기
            val builder = SpannableStringBuilder(deleteAskTextView!!.text)
            builder.setSpan(ForegroundColorSpan(Color.parseColor("#F36E6E")) , 7,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            deleteAskTextView.setText(builder)
            deleteConfirmButton!!.setOnClickListener {
                deleteRcoin()       //삭제눌렀을때 삭제
                deleteAlertDialog.dismiss()
            }

            cancelButton!!.setOnClickListener {
                deleteAlertDialog.dismiss()
            }
        }
    }


    override fun rptCoinInsquireSuccess(response: RptCoinMgtInsquireResponse) {
        dismissLoadingDialog()
        Log.d("InsquireSuccess" , "${response}")
        coinList = response.result
        //널값 넘어오면서 오류
        //Log.d("psnSuccesscoinSize", "${coinList.size}")
        // 코인 시세 받아오기 단 , 대표코인이 없을 경우 실행하지 않음.
        if(coinList.size != 0){
            getCoinPrice()
        }
        setRecyclerView()
    }

    override fun rptCoinInsquireFailure(message: String) {
        dismissLoadingDialog()
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }

    //삭제 성공
    override fun deleteRptCoinSuccess(response: DeleteRptCoinResponse) {
        cnt++
        Log.d("deleteRCoin", "삭제 리스트 추가 : ${deleteRcoinList.size}")
        if(cnt == deleteRcoinList.size){
            dismissLoadingDialog()
            getRptCoins()           //삭제 완료됐으면 재호출
            deleteRcoinList.clear() //삭제 리스트 초기화
            Log.d("deleteRCoin", "삭제 리스트 추가 : ${deleteRcoinList.size}")
            cnt = 0;
        }

    }
    //삭제 실패
    override fun deleteRptCoinFailure(message: String) {
        dismissLoadingDialog()
        showToast(message)
    }
    // 시세 받아오기
    fun getCoinPrice() {
        // 대표코인 이름 저장
        for (i in 0 until coinList.size) {
            coinSymbolSet.add(coinList[i].symbol)
        }
        // 웹 소켓 연결
        when (marketName) {
            "업비트" -> {
                upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
                upbitWebSocket?.setCoinView(this)
                upbitWebSocket?.start() // 업비트 웹 소켓 실행
                coinService.getUpbitCurrentPrice(null,coinList) // 업비트 코인 시세 받아오기

            }
            "빗썸" -> {
                bithumbWebSocket = BithumbWebSocketListener(coinSymbolSet)
                bithumbWebSocket?.setCoinView(this)
                bithumbWebSocket?.start() // 빗썸 웹 소켓 실행
                coinService.getBithumbCurrentPrice(null,coinList) // 빗썸 코인 시세 받아오기
            }
        }
        binanceWebSocket = BinanceWebSocketListener(coinSymbolSet)
        binanceWebSocket?.setCoinView(this)
        binanceWebSocket?.start()
    }

    // 업비트 시세 조회 API 호출 성공
    override fun marketPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        var position = 0
        if(requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // 대표 코인
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (upbitCoinPriceMap.containsKey(symbol)) {
                        val change = upbitCoinPriceMap.get(symbol+"change")
                        coinList[i].marketPrice = upbitCoinPriceMap.get(symbol)!!
                        if (change != null) {
                            coinList[i].change = change
                        }
                        position = i
                    }
                }
                viewModel.getUpdateRepresentCoin(coinList, position)
            }
        }
    }

    // 바이낸스 시세 조회 API 호출 성공
    override fun binancePriceSuccess(binanceCoinPriceMap: HashMap<String, Double>) {
        var position = 0
        if(requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // 대표 코인
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (binanceCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = coinList[i].marketPrice
                        val binancePrice = binanceCoinPriceMap.get(symbol)!! * usdtPrice!!
                        var kimchi = ((upbitPrice - binancePrice) / upbitPrice) * 100
                        coinList[i].binancePrice = binancePrice
                        coinList[i].kimchi = kimchi
                        position = i
                    }
                }
                viewModel.getUpdateRepresentCoin(coinList, position)
            }
        }
    }

    override fun coinPriceFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cnt = 0
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}