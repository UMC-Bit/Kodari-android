package com.bit.kodari.PossessionCoin

//import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementRVAdapter
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

class PossessionCoinManagementFragment(val accountName:String) :BaseFragment<FragmentPossessionCoinManagementBinding>(FragmentPossessionCoinManagementBinding::inflate), PsnCoinMgtInsquireView, PsnCoinMgtDeleteView,
CoinView{
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var checkView = true
    private var coinSymbolSet = HashSet<String>()    // 유저 코인, 대표 코인 심볼 저장
    var upbitWebSocket: UpbitWebSocketListener? = null    // 업비트 웹 소켓
    var binanceWebSocket: BinanceWebSocketListener? = null // 바이낸스 웹 소켓
    private lateinit var possessionCoinManagementAdapter: PossessionCoinManagementAdapter
    private var coinList = ArrayList<PossesionCoinResult>()
    override fun initAfterBinding() {
        setListener()
        getPossessionCoins()
        // ViewModel 적용
        viewModelFactory = CoinViewModelFactory(coinList, null)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        viewModel.userCoinData.observe(this, androidx.lifecycle.Observer {
            setRecyclerView()
        })
    }
    override fun onPause() {
        super.onPause()
        checkView = false
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
                    .replace(R.id.main_container_fl, PossessionCoinModifyFragment(accountName).apply {
                        arguments = Bundle().apply {
                            putInt("coinIdx", coinList[position].coinIdx)
                            Log.d("checkcoinidx", "${coinList[position]}")
                            putString("coinImage", coinList[position].coinImg)
                            putString("coinName", coinList[position].coinName)
                            putString("coinSymbol", coinList[position].symbol)
                            putDouble("priceAvg", coinList[position].priceAvg)
                            putDouble("amount", coinList[position].amount)
                        }
                    }).commitAllowingStateLoss()
            }
        }

        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            // 선택 버튼 클릭 시에만 삭제 다이얼로그가 띄워짐
            val isClick = PossessionCoinManagementAdapter.isClick
            Log.d("PossessiongCoinDelete", "실행 ,  체크 : ${isClick}")
            val position = PossessionCoinManagementAdapter.clickPosition
            Log.d("PossessiongCoinDelete", "실행 , ${position} , 체크 : ${isClick}")
            if (isClick && position != -1) {
                val deleteDialogView = LayoutInflater.from(context as MainActivity)
                    .inflate(R.layout.fragment_possession_coin_delete_dialog, null)
                val deleteDialogBuilder = AlertDialog.Builder(context as MainActivity)
                    .setView(deleteDialogView)
                val deleteAlertDialog = deleteDialogBuilder.show()
                val position = PossessionCoinManagementAdapter.clickPosition
                deleteDialog(coinList[position].userCoinIdx)
                deleteAlertDialog.dismiss()
            }
        }

        binding.possessionCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment(accountName)).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }
    }

    fun deleteDialog(userCoinIdx: Int) {
        val deleteDialogView=LayoutInflater.from(context as MainActivity).inflate(R.layout.fragment_possession_coin_delete_dialog, null)
        val deleteDialogBuilder=AlertDialog.Builder(context as MainActivity)
            .setView(deleteDialogView)

        val deleteAlertDialog = deleteDialogBuilder.show()

        val deleteConfirmButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
        val cancelButton=deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)
        val deleteAskTextView = deleteDialogView.findViewById<TextView>(R.id.possession_coin_delete_dialog_ask_TV)

        //글자 색 바꾸기
        val builder = SpannableStringBuilder(deleteAskTextView.text)
        builder.setSpan(ForegroundColorSpan(Color.RED) , 7,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        deleteAskTextView.setText(builder)
        // 여기에 어댑터와 연결해서 삭제 기능 불러오기
        deleteConfirmButton.setOnClickListener {
                // 소유코인 삭제 API 호출 ->
                val psnCoinService = PsnCoinService()
                psnCoinService.setPsnCoinMgtDeleteView(this)
                showLoadingDialog(requireContext())
                psnCoinService.deletePsnCoin(userCoinIdx)
                deleteAlertDialog.dismiss()
            }
        cancelButton.setOnClickListener {
            deleteAlertDialog.dismiss()
        }
    }

    fun setRecyclerView(){
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
                    .replace(R.id.main_container_fl , PossessionCoinManagementFragment(accountName)).commitAllowingStateLoss()

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
                }
                viewModel.getUpdateUserCoin(coinList)
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
}