package com.bit.kodari.Util.Upbit

import android.util.Log
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Portfolio.Adapter.SearchCoinView
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Util.Binance.BinanceResult
import com.bit.kodari.Util.Coin.CoinInterface
import com.bit.kodari.Util.Coin.CoinView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinService {

    private lateinit var coinView: CoinView

    fun setCoinView(coinView: CoinView){
        this.coinView = coinView
    }
    // BASE URL
    val BASE_URL_UPBIT_API = "https://api.upbit.com/"

    /*
        현재 코인 시세 API 받아오기
        업비트, 바이낸스
        소유코인, 대표코인
        코인 List를 받아서 현재 코인 가격 List를 반환하는 함수
     */
    fun getCurrentPrice(userCoinList: List<PossesionCoinResult>, representCoinList: List<RepresentCoinResult>){
        val currentPriceList = ArrayList<Double>()
        // Retrofit 초기 설정
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_UPBIT_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(CoinInterface::class.java)
        // userCoinList 크기만큼 반복문 실행, 현재 가격을 userCoinList.price에 추가
        for(i in 0 until userCoinList.size){
            // 업비트 유저 코인시세 받아오기
            val callGetUpbitPrice = api.getCurrentUpbitPrice("application/json", "KRW-"+userCoinList[i].symbol)
            callGetUpbitPrice.enqueue(object : Callback<List<UpbitPrice>> {
                override fun onResponse(
                    call: Call<List<UpbitPrice>>,
                    response: Response<List<UpbitPrice>>
                ) {
                    // 코인의 현재 가격을 받아옴
                    val price: Double? = response.body()?.get(0)?.trade_price
                    if (price != null) {
                        userCoinList[i].upbitPrice = price
                    }else{
                        userCoinList[i].upbitPrice = 0.0
                    }
                    val homeFragment = HomeFragment()
                    Log.d("결과", "성공: ${response.body()}")

                }
                override fun onFailure(call: Call<List<UpbitPrice>>, t: Throwable) {
                    Log.d("결과", "실패")
                }
            })
            // 바이낸스 유저 코인시세 받아오기
            val callBinancePrice = api.getCurrentBinancePrice();
            callBinancePrice.enqueue(object : Callback<List<BinanceResult>> {
                override fun onResponse(
                    call: Call<List<BinanceResult>>,
                    response: Response<List<BinanceResult>>
                ) {
                    // 코인 리스트를 받아와서 coinList에 있는 Symbol이랑 비교하기 위한 for문
                    for(j in response.body()!!.indices){
                        val coinSymbol = response.body()!![j].symbol
                        if((userCoinList[i].symbol+"USDT").equals(coinSymbol)){
                            val price = response.body()!![j].price
                            userCoinList[i].binancePrice = price
                            Log.d("결과", "성공: ${coinSymbol}: ${price}")
                        }
                    }
                }
                override fun onFailure(call: Call<List<BinanceResult>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        // representCoinList 크기만큼 반복문 실행, 현재 가격을 representCoinList.price에 추가
        for(i in 0 until representCoinList.size){
            // 업비트 대표코인 시세 받아오기
            val callGetUpbitPrice = api.getCurrentUpbitPrice("application/json", "KRW-"+userCoinList[i].symbol)
            callGetUpbitPrice.enqueue(object : Callback<List<UpbitPrice>> {
                override fun onResponse(
                    call: Call<List<UpbitPrice>>,
                    response: Response<List<UpbitPrice>>
                ) {
                    // 코인의 현재 가격을 받아옴
                    val price: Double? = response.body()?.get(0)?.trade_price
                    if (price != null) {
                        representCoinList[i].upbitPrice = price
                    }else{
                        representCoinList[i].upbitPrice = 0.0
                    }
                    val homeFragment = HomeFragment()
                    Log.d("결과", "성공: ${response.body()}")
                }
                override fun onFailure(call: Call<List<UpbitPrice>>, t: Throwable) {
                    Log.d("결과", "실패")
                }
            })
            // 바이낸스 대표코인 시세 받아오기
            val callBinancePrice = api.getCurrentBinancePrice();
            callBinancePrice.enqueue(object : Callback<List<BinanceResult>> {
                override fun onResponse(
                    call: Call<List<BinanceResult>>,
                    response: Response<List<BinanceResult>>
                ) {
                    // 코인 리스트를 받아와서 coinList에 있는 Symbol이랑 비교하기 위한 for문
                    for(j in response.body()!!.indices){
                        val coinSymbol = response.body()!![j].symbol
                        if((representCoinList[i].symbol+"USDT").equals(coinSymbol)){
                            val price = response.body()!![j].price
                            representCoinList[i].binancePrice = price
                            Log.d("결과", "성공: ${coinSymbol}: ${price}")
                        }
                    }
                }
                override fun onFailure(call: Call<List<BinanceResult>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}

