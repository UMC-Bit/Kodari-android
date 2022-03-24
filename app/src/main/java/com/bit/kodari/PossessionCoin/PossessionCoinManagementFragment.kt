package com.bit.kodari.PossessionCoin

//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtDeleteView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtInsquireView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtDeleteResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.Util.Coin.*
import com.bit.kodari.Util.Coin.Binance.BinanceWebSocketListener
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PossessionCoinManagementFragment(val accountName:String , val marketIdx: Int) :BaseFragment<FragmentPossessionCoinManagementBinding>(FragmentPossessionCoinManagementBinding::inflate), PsnCoinMgtInsquireView, PsnCoinMgtDeleteView,
CoinView{
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var checkView = true
    private var coinSymbolSet = HashSet<String>()    // 유저 코인, 대표 코인 심볼 저장
    var upbitWebSocket: UpbitWebSocketListener? = null    // 업비트 웹 소켓
    var binanceWebSocket: BinanceWebSocketListener? = null // 바이낸스 웹 소켓
    private var coinList = ArrayList<PossesionCoinResult>()
    private var possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
    private lateinit var callback:OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, HomeFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        setListener()
        getPossessionCoins()
        // ViewModel 적용
        viewModelFactory = CoinViewModelFactory(coinList, null)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        viewModel.userCoinData.observe(this, androidx.lifecycle.Observer {
            if(possessionCoinManagementAdapter != null){
                var position = viewModel.getUserCoinPosition()
                possessionCoinManagementAdapter.setData(coinList, position)
            }else {
                setRecyclerView()
            }
        })
    }
    override fun onPause() {
        super.onPause()
        checkView = false
        PossessionCoinManagementAdapter.clickPosition = -1
    }

    override fun onResume() {
        super.onResume()
        checkView = true
    }

    override fun onDestroy() {
        super.onDestroy()
        checkView = false
        upbitWebSocket?.webSocket?.cancel() // 웹 소켓 닫기
        binanceWebSocket?.webSocket?.cancel()
    }

    fun setListener(){
        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            // 선택 버튼 클릭 시에만 수정 fragement로 이동 가능
            val isClick = PossessionCoinManagementAdapter.isClick
            val position = PossessionCoinManagementAdapter.clickPosition
            if (isClick && position != -1) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinModifyFragment(accountName,marketIdx).apply {
                        arguments = Bundle().apply {
                            putInt("coinIdx", coinList[position].coinIdx)
                            Log.d("checkcoinidx", "${coinList[position]}")
                            putString("coinImage", coinList[position].coinImg)
                            putString("coinName", coinList[position].coinName)
                            putString("coinSymbol", coinList[position].symbol)
                            putDouble("priceAvg", coinList[position].priceAvg)
                            putDouble("amount", coinList[position].amount)
                            Log.d("amout", "매니지 : ${coinList[position].amount}")
                        }
                    }).commit()
            }
        }

        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            // 선택 버튼 클릭 시에만 삭제 다이얼로그가 띄워짐
            val isClick = PossessionCoinManagementAdapter.isClick
            Log.d("PossessiongCoinDelete", "실행 ,  체크 : ${isClick}")
            val position = PossessionCoinManagementAdapter.clickPosition
            Log.d("PossessiongCoinDelete", "실행 , ${position} , 체크 : ${isClick}")
            if (isClick && position != -1) {
                val position = PossessionCoinManagementAdapter.clickPosition
                deleteDialog(coinList[position].userCoinIdx)
            }
        }

        binding.possessionCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment(accountName ,marketIdx)).commit()
        }

        binding.possessionCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commit()
        }
    }

    fun deleteDialog(userCoinIdx: Int) {
        val deleteAlertDialog = MaterialAlertDialogBuilder(context as MainActivity, R.style.MyRounded_MaterialComponents_MaterialAlertDialog)
            .setView(R.layout.fragment_possession_coin_delete_dialog).show()
        val deleteConfirmButton=deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
        val cancelButton=deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)
        val deleteAskTextView = deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_ask_TV)

        //글자 색 바꾸기
        val builder = SpannableStringBuilder(deleteAskTextView!!.text)
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#F36E6E")) , 7,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        deleteAskTextView.setText(builder)

        // 여기에 어댑터와 연결해서 삭제 기능 불러오기
        deleteConfirmButton!!.setOnClickListener {
                // 소유코인 삭제 API 호출 ->
                val psnCoinService = PsnCoinService()
                psnCoinService.setPsnCoinMgtDeleteView(this)
                showLoadingDialog(requireContext())
                psnCoinService.deletePsnCoin(userCoinIdx)
                deleteAlertDialog.dismiss()
            }
        cancelButton!!.setOnClickListener {
            deleteAlertDialog.dismiss()
        }
    }
    fun setRecyclerView(){
        val animator = binding.possessionCoinManagementRV.itemAnimator // 애니메이션 제거
        if(animator is SimpleItemAnimator) { //아이템 애니메이커 기본 하위클래스
            animator.supportsChangeAnimations =
                false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
        }
        possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        possessionCoinManagementAdapter.setMyItemClickListener(object :
            PossessionCoinManagementAdapter.MyItemClickListener{
            override fun onItemClick(item: PossesionCoinResult) {

            }
        })
        if(checkView) {
            binding.possessionCoinManagementRV.layoutManager =
                LinearLayoutManager(context as MainActivity)
            binding.possessionCoinManagementRV.adapter = possessionCoinManagementAdapter
        }
    }

    fun getPossessionCoins(){
        val psnCoinService = PsnCoinService()
        psnCoinService.setPsnCoinMgtInsquireView(this)
        showLoadingDialog(requireContext())
        psnCoinService.getPsnCoinMgtInsquire(MyApplicationClass.myPortIdx)
    }

    override fun psnCoinInsquireSuccess(response: PsnCoinMgtInsquireResponse) {
        Log.d("InsquireSuccess" , "${response}")
        dismissLoadingDialog()
        coinList = response.result
        //Log.d("psnSuccesscoinSize", "${coinList.size}")
        getCoinPrice()
        setRecyclerView()
    }

    override fun psnCoinInsquireFailure(message: String) {
        dismissLoadingDialog()
        Log.d("InsquireFailure", "코인 목록 불러오기 실패, ${message}")
    }

    override fun deletePsnCoinSuccess(response: PsnCoinMgtDeleteResponse) {
        dismissLoadingDialog()
        when(response.code){
            1000 -> {
                PossessionCoinManagementAdapter.isClick=false
                PossessionCoinManagementAdapter.clickPosition=-1
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , PossessionCoinManagementFragment(accountName ,marketIdx)).commitAllowingStateLoss()

            }
            else -> {
                Toast.makeText(context, "${response.message}" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun deletePsnCoinFailure(message: String) {
        dismissLoadingDialog()
        Log.d("deltePsnCoinFail" ,"${message}")
    }
    // 시세 받아오기
    fun getCoinPrice() {
        // 대표코인 이름 저장
        for (i in 0 until coinList.size) {
            coinSymbolSet.add(coinList[i].symbol)
        }

        // 웹 소켓 연결
        upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
        upbitWebSocket?.setCoinView(this)
        upbitWebSocket?.start() // 업비트 웹 소켓 실행
    }

    // 업비트 시세 조회 API 호출 성공
    override fun upbitPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        var position = 0
        if(requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // 소유 코인
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (upbitCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = upbitCoinPriceMap.get(symbol)!!
                        val change = upbitCoinPriceMap.get(symbol+"change")
                        val amount = coinList[i].amount
                        val priceAvg = coinList[i].priceAvg
                        coinList[i].upbitPrice = upbitPrice
                        if (change != null) {
                            coinList[i].change = change
                        }
                        coinList[i].profit = getProfit(upbitPrice, amount, priceAvg)
                    }
                    position = i;
                }
                viewModel.getUpdateUserCoin(coinList, position)
            }
        }
    }

    override fun binancePriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        TODO("Not yet implemented")
    }
    override fun coinPriceFailure(message: String) {
        TODO("Not yet implemented")
    }
    // 평가순익 구하는 메서드
    fun getProfit(currentPrice: Double, priceAvg: Double, amount: Double): Double {
        return (currentPrice * amount) - (priceAvg * amount)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}