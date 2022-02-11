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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CoinService {

    private lateinit var coinView: CoinView

    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }

    // BASE URL
    val BASE_URL_UPBIT_API = "https://api.upbit.com/"
    val BASE_URL_BINANCE_API = "https://api.binance.com"

    /*
        현재 코인 시세 API 받아오기
        업비트, 바이낸스
        소유코인, 대표코인
        코인 List를 받아서 현재 코인 가격 List를 반환하는 함수
     */
    fun getCurrentPrice(
        userCoinList: List<PossesionCoinResult>,
        representCoinList: List<RepresentCoinResult>
    ) {
        val currentPriceList = ArrayList<Double>()
        // Retrofit 초기 설정
        val upbitRetrofit = Retrofit.Builder()         // upbit
            .baseUrl(BASE_URL_UPBIT_API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val upbitApi = upbitRetrofit.create(CoinInterface::class.java)
        val binanceRetrofit = Retrofit.Builder()         // binance
            .baseUrl(BASE_URL_BINANCE_API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val binanceApi = binanceRetrofit.create(CoinInterface::class.java)
        // userCoinList 크기만큼 반복문 실행, 현재 가격을 userCoinList.price에 추가
        for (i in 0 until userCoinList.size) {
            // 업비트 유저 코인시세 받아오기
            upbitApi.getCurrentUpbitPrice("application/json", "KRW-" + userCoinList[i].symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.delay(200L, TimeUnit.MILLISECONDS)
                //.repeat()
                .subscribe({
                    val price = it[0].trade_price
                    if (price != null) {
                        userCoinList[i].upbitPrice = price
                    } else {
                        userCoinList[i].upbitPrice = 0.0
                    }
                    val homeFragment = HomeFragment()
                    Log.d("결과", "성공: ${price}")
                }, {
                    Log.d("실패", "업비트 시세 조회 실패")
                })
            // 바이낸스 유저 코인시세 받아오기
            binanceApi.getCurrentBinancePrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.delay(200L, TimeUnit.MILLISECONDS)
                //.repeat()
                .subscribe({
                    for (j in it.indices) {
                        val coinSymbol = it[j].symbol
                        if ((userCoinList[i].symbol + "USDT").equals(coinSymbol)) {
                            val price = it[j].price
                            userCoinList[i].binancePrice = price
                            Log.d("결과", "성공: ${coinSymbol}: ${price}")
                        }
                    }
                },
                    {
                        Log.d("실패", "${it.message} 바이낸스 시세 조회 실패")
                    })

            // representCoinList 크기만큼 반복문 실행, 현재 가격을 representCoinList.price에 추가
            for (i in 0 until representCoinList.size) {
                upbitApi.getCurrentUpbitPrice(
                    "application/json",
                    "KRW-" + representCoinList[i].symbol
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .delay(200L, TimeUnit.MILLISECONDS)
                    .repeat()
                    .subscribe({
                        val price = it[0].trade_price
                        if (price != null) {
                            representCoinList[i].upbitPrice = price
                        } else {
                            representCoinList[i].upbitPrice = 0.0
                        }
                        val homeFragment = HomeFragment()
                        Log.d("결과", "성공: ${price}")
                    }, {
                        Log.d("실패", "업비트 시세 조회 실패")
                    })

                // 바이낸스 대표코인 시세 받아오기
                binanceApi.getCurrentBinancePrice()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.delay(500L, TimeUnit.MILLISECONDS)
                    .repeat()
                    .subscribe({
                        for (j in it.indices) {
                            val coinSymbol = it[j].symbol
                            if ((representCoinList[i].symbol + "USDT").equals(coinSymbol)) {
                                val price = it[j].price
                                representCoinList[i].binancePrice = price
                                Log.d("결과", "성공: ${coinSymbol}: ${price}")
                                coinView.coinPriceSuccess(userCoinList, representCoinList)
                            }
                        }
                    },
                        {
                            Log.d("실패", "바이낸스 시세 조회 실패")
                        })
            }
        }
    }
}

