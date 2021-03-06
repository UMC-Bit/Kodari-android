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
import com.bit.kodari.Main.Data.RepresentCoinResult
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
import com.bit.kodari.Util.Coin.Bithumb.BithumbWebSocketListener
import com.bit.kodari.Util.Coin.Upbit.UpbitWebSocketListener
import com.bit.kodari.Util.Upbit.CoinService
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PossessionCoinManagementFragment(val accountName: String, val marketIdx: Int) :
    BaseFragment<FragmentPossessionCoinManagementBinding>(FragmentPossessionCoinManagementBinding::inflate),
    PsnCoinMgtInsquireView, PsnCoinMgtDeleteView, CoinView {
    private lateinit var viewModel: CoinViewModel
    private lateinit var viewModelFactory: CoinViewModelFactory
    private var checkView = true
    private var coinSymbolSet = HashSet<String>()    // ?????? ??????, ?????? ?????? ?????? ??????
    private val marketName = MyApplicationClass.marketName // ????????? ??????
    var upbitWebSocket: UpbitWebSocketListener? = null    // ????????? ??? ??????
    var bithumbWebSocket: BithumbWebSocketListener? = null    // ????????? ??? ??????
    var binanceWebSocket: BinanceWebSocketListener? = null // ???????????? ??? ??????
    private var coinList = ArrayList<PossesionCoinResult>()
    private var possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
    private lateinit var callback: OnBackPressedCallback
    private lateinit var coinService: CoinService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, HomeFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun initAfterBinding() {
        setListener()
        getPossessionCoins()
        // ViewModel ??????
        viewModelFactory = CoinViewModelFactory(coinList, null)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CoinViewModel::class.java)
        // ????????? ?????? ?????? ????????????
        coinService = CoinService()
        coinService.setViewModel(viewModel)
        viewModel.userCoinData.observe(this, androidx.lifecycle.Observer {
            var position = viewModel.getUserCoinPosition()
            if (possessionCoinManagementAdapter != null && coinList.size > position) {
                // ?????? ?????? ?????? ????????????, ????????? ?????????
                val marketPrice = coinList[position].marketPrice
                if(marketPrice == 0.0)
                    return@Observer
                val priceAvg = coinList[position].priceAvg
                val amount = coinList[position].amount
                coinList[position].profit = getProfit(marketPrice, priceAvg, amount)
                possessionCoinManagementAdapter.setData(coinList, position)
            } else {
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
        upbitWebSocket?.webSocket?.cancel() // ??? ?????? ??????
        bithumbWebSocket?.webSocket?.cancel()
        binanceWebSocket?.webSocket?.cancel()
    }

    fun setListener() {
        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            // ?????? ?????? ?????? ????????? ?????? fragement??? ?????? ??????
            val isClick = PossessionCoinManagementAdapter.isClick
            val position = PossessionCoinManagementAdapter.clickPosition
            if (isClick && position != -1) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container_fl,
                        PossessionCoinModifyFragment(accountName, marketIdx).apply {
                            arguments = Bundle().apply {
                                putInt("coinIdx", coinList[position].coinIdx)
                                Log.d("checkcoinidx", "${coinList[position]}")
                                putString("coinImage", coinList[position].coinImg)
                                putString("coinName", coinList[position].coinName)
                                putString("coinSymbol", coinList[position].symbol)
                                putDouble("priceAvg", coinList[position].priceAvg)
                                putDouble("amount", coinList[position].amount)
                            }
                        }).commit()
            }
        }

        binding.possessionCoinManagementDeleteButtonIB.setOnClickListener {
            // ?????? ?????? ?????? ????????? ?????? ?????????????????? ?????????
            val isClick = PossessionCoinManagementAdapter.isClick
            val position = PossessionCoinManagementAdapter.clickPosition
            if (isClick && position != -1) {
                val position = PossessionCoinManagementAdapter.clickPosition
                deleteDialog(coinList[position].userCoinIdx)
            }
        }

        binding.possessionCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_container_fl,
                    PossessionCoinSearchFragment(accountName, marketIdx)
                ).commit()
        }

        binding.possessionCoinManagementBeforeButtonBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commit()
        }
    }

    fun deleteDialog(userCoinIdx: Int) {
        val deleteAlertDialog = MaterialAlertDialogBuilder(
            context as MainActivity,
            R.style.MyRounded_MaterialComponents_MaterialAlertDialog
        )
            .setView(R.layout.fragment_possession_coin_delete_dialog).show()
        val deleteConfirmButton =
            deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_delete_confirm_TV)
        val cancelButton =
            deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_cancel_TV)
        val deleteAskTextView =
            deleteAlertDialog.findViewById<TextView>(R.id.possession_coin_delete_dialog_ask_TV)

        //?????? ??? ?????????
        val builder = SpannableStringBuilder(deleteAskTextView!!.text)
        builder.setSpan(
            ForegroundColorSpan(Color.parseColor("#F36E6E")),
            7,
            9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        deleteAskTextView.setText(builder)

        // ????????? ???????????? ???????????? ?????? ?????? ????????????
        deleteConfirmButton!!.setOnClickListener {
            // ???????????? ?????? API ?????? ->
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

    fun setRecyclerView() {
        val animator = binding.possessionCoinManagementRV.itemAnimator // ??????????????? ??????
        if (animator is SimpleItemAnimator) { //????????? ??????????????? ?????? ???????????????
            animator.supportsChangeAnimations =
                false  //??????????????? ??? false (????????????????????? ????????? ?????? ?????? ????????? ????????? ????????? ??????)
        }
        possessionCoinManagementAdapter = PossessionCoinManagementAdapter(coinList)
        //????????? ?????? ???????????? ?????? ????????? ??????
        //Adapter??? ?????? position?????? ?????? HomeFragment??? ???????????? ?????? ??????
        possessionCoinManagementAdapter.setMyItemClickListener(object :
            PossessionCoinManagementAdapter.MyItemClickListener {
            override fun onItemClick(item: PossesionCoinResult) {

            }
        })
        if (checkView) {
            binding.possessionCoinManagementRV.layoutManager =
                LinearLayoutManager(context as MainActivity)
            binding.possessionCoinManagementRV.adapter = possessionCoinManagementAdapter
        }
    }

    fun getPossessionCoins() {
        showLoadingDialog(requireContext())
        val psnCoinService = PsnCoinService()
        psnCoinService.setPsnCoinMgtInsquireView(this)
        psnCoinService.getPsnCoinMgtInsquire(MyApplicationClass.myPortIdx)
    }

    override fun psnCoinInsquireSuccess(response: PsnCoinMgtInsquireResponse) {
        dismissLoadingDialog()
        coinList = response.result
        //Log.d("psnSuccesscoinSize", "${coinList.size}")
        getCoinPrice()
        setRecyclerView()
    }

    override fun psnCoinInsquireFailure(message: String) {
        dismissLoadingDialog()
        Log.d("InsquireFailure", "?????? ?????? ???????????? ??????, ${message}")
    }

    override fun deletePsnCoinSuccess(response: PsnCoinMgtDeleteResponse) {
        dismissLoadingDialog()
        when (response.code) {
            1000 -> {
                PossessionCoinManagementAdapter.isClick = false
                PossessionCoinManagementAdapter.clickPosition = -1
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container_fl,
                        PossessionCoinManagementFragment(accountName, marketIdx)
                    )
                    .commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun deletePsnCoinFailure(message: String) {
        dismissLoadingDialog()
        Log.d("deltePsnCoinFail", "${message}")
    }

    // ?????? ????????????
    fun getCoinPrice() {
        // ???????????? ?????? ??????
        for (i in 0 until coinList.size) {
            coinSymbolSet.add(coinList[i].symbol)
        }
        when (marketName) {
            "?????????" -> {
                upbitWebSocket = UpbitWebSocketListener(coinSymbolSet)
                upbitWebSocket?.setCoinView(this)
                upbitWebSocket?.start() // ????????? ??? ?????? ??????
                coinService.getUpbitCurrentPrice(coinList, null) // ????????? ?????? ?????? ????????????
            }
            "??????" -> {
                bithumbWebSocket = BithumbWebSocketListener(coinSymbolSet)
                bithumbWebSocket?.setCoinView(this)
                bithumbWebSocket?.start() // ?????? ??? ?????? ??????
                coinService.getBithumbCurrentPrice(coinList, null) // ?????? ?????? ?????? ????????????
            }
        }
    }

    // ????????? ?????? ?????? API ?????? ??????
    override fun marketPriceSuccess(marketCoinPriceMap: HashMap<String, Double>) {
        var position = 0
        if (requireActivity() != null) {
            requireActivity().runOnUiThread() {
                // ?????? ??????
                for (i in coinList.indices) {
                    val symbol = coinList[i].symbol
                    if (marketCoinPriceMap.containsKey(symbol)) {
                        val marketPrice = marketCoinPriceMap.get(symbol)!!
                        val change = marketCoinPriceMap.get(symbol + "change")
                        val amount = coinList[i].amount
                        val priceAvg = coinList[i].priceAvg
                        coinList[i].marketPrice = marketPrice
                        if (change != null) {
                            coinList[i].change = change
                        }
                        coinList[i].profit = getProfit(marketPrice, priceAvg, amount)
                        position = i;
                    }
                }
                viewModel.getUpdateUserCoin(coinList, position)
            }
        }
    }


    override fun binancePriceSuccess(upbitCoinPriceMap: HashMap<String, Double>) {

    }

    override fun coinPriceFailure(message: String) {

    }

    // ???????????? ????????? ?????????
    fun getProfit(currentPrice: Double, priceAvg: Double, amount: Double): Double {
        return (currentPrice * amount) - (priceAvg * amount)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}