package com.bit.kodari.RepresentativeCoin

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Adapter.RptCoinManagementAdapter
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinMgtInsquireView
import com.bit.kodari.RepresentativeCoin.RetrofitData.DeleteRptCoinResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.Util.Coin.*
import com.bit.kodari.Util.Coin.Binance.BinanceWebSocketListener
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.bit.kodari.databinding.FragmentRepresentativeCoinManagementBinding

//삭제 누르면 끝나고 작업 다시 재조회 -> deletList 초기화
class RepresentativeCoinManagementFragment : BaseFragment<FragmentRepresentativeCoinManagementBinding>(FragmentRepresentativeCoinManagementBinding::inflate), RptCoinMgtInsquireView,
    CoinView {
    var usdtPrice = HomeFragment.usdtPrice
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var coinSymbolSet = HashSet<String>()    // 유저 코인, 대표 코인 심볼 저장
    var upbitWebSocket: UpbitWebSocketListener? = null    // 업비트 웹 소켓
    var binanceWebSocket: BinanceWebSocketListener? = null // 바이낸스 웹 소켓
    private lateinit var rptCoinManagementAdapter: RptCoinManagementAdapter
    private var coinList = ArrayList<RepresentCoinResult>()
    private var deleteRcoinList = HashSet<Int>()      //대표코인 삭제할 리스트들
    companion object{
        private var cnt = 0
    }
    override fun initAfterBinding() {
        // Usdt 환율 받아옴
        deleteDialog()
        setListener()
        getRptCoins()
        // ViewModel 적용
        viewModelFactory = CoinViewModelFactory(null, coinList)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        viewModel.representCoinData.observe(this, androidx.lifecycle.Observer {
            setRecyclerView()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        upbitWebSocket?.webSocket?.cancel() // 웹 소켓 닫기
        binanceWebSocket?.webSocket?.cancel()
    }
    fun setListener(){
        binding.representativeCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinSearchFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.representativeCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }


    }

    fun setRecyclerView(){
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
            val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_representative_coin_delete_dialog, null)
            val deleteDialogBuilder= AlertDialog.Builder(context as MainActivity)
                .setView(deleteDialogView)

            val deleteAlertDialog = deleteDialogBuilder.show()

            val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_delete_confirm_TV)
            val cancelButton=deleteDialogView.findViewById<TextView>(R.id.representative_coin_delete_dialog_cancel_TV)

            deleteConfirmButton.setOnClickListener {
                deleteRcoin()       //삭제눌렀을때 삭제
                deleteAlertDialog.dismiss()
            }

            cancelButton.setOnClickListener {
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
        upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
        upbitWebSocket?.setCoinView(this)
        upbitWebSocket?.start() // 업비트 웹 소켓 실행
        binanceWebSocket = BinanceWebSocketListener(coinSymbolSet)
        binanceWebSocket?.setCoinView(this)
        binanceWebSocket?.start()
    }

    // 업비트 시세 조회 API 호출 성공
    override fun upbitPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {
        if(requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // 대표 코인
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (upbitCoinPriceMap.containsKey(symbol)) {
                        val change = upbitCoinPriceMap.get(symbol+"change")
                        coinList[i].upbitPrice = upbitCoinPriceMap.get(symbol)!!
                        if (change != null) {
                            coinList[i].change = change
                        }
                    }
                }
                viewModel.getUpdateRepresentCoin(coinList)
            }
        }
    }
    // 바이낸스 시세 조회 API 호출 성공
    override fun binancePriceSuccess(binanceCoinPriceMap: HashMap<String, Double>) {
        if(requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // 대표 코인
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (binanceCoinPriceMap.containsKey(symbol)) {
                        val upbitPrice = coinList[i].upbitPrice
                        val binancePrice = binanceCoinPriceMap.get(symbol)!! * usdtPrice!!
                        var kimchi = ((upbitPrice - binancePrice) / upbitPrice) * 100
                        coinList[i].binancePrice = binancePrice
                        coinList[i].kimchi = kimchi
                    }
                }
                viewModel.getUpdateRepresentCoin(coinList)
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
}