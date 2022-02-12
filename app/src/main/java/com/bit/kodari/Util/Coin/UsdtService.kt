package com.bit.kodari.Util.Coin

import android.util.Log
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Main.HomeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.newFixedThreadPoolContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class UsdtService {
    private lateinit var coinView: CoinView
    private var KRW_BTC: Double = 1.0
    private var BTC_USDT: Double = 1.0
    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }

    // BASE URL
    val BASE_URL_UPBIT_API = "https://api.upbit.com/"

    /*
        현재 USDT 시세 받아오기
        KRW-USDT = (KRW-BTC / BTC-USDT)
     */
    fun getUsdtPrice(
    ) {
        val currentPriceList = ArrayList<Double>()
        // Retrofit 초기 설정
        val upbitRetrofit = Retrofit.Builder()         // upbit
            .baseUrl(BASE_URL_UPBIT_API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val upbitBtcApi = upbitRetrofit.create(CoinInterface::class.java)
        val upbitUsdtApi = upbitRetrofit.create(CoinInterface::class.java)
            // 업비트 유저 코인시세 받아오기
            upbitBtcApi.getCurrentUpbitPrice("application/json", "KRW-BTC")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1000L, TimeUnit.MILLISECONDS)
                .repeat()
                .subscribe({
                    val price = it[0].trade_price
                    if (price != null) {
                            KRW_BTC = price
                        }
                    Log.d("결과", "성공: KRW-BTC: ${BigDecimal(price).toPlainString()}")
                }, {
                    Log.d("실패", "업비트 시세 조회 실패")
                })
        upbitUsdtApi.getCurrentUpbitPrice("application/json", "USDT-BTC")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1000L, TimeUnit.MILLISECONDS)
            .repeat()
            .subscribe({
                val price = it[0].trade_price
                if (price != null) {
                        BTC_USDT = price
                }
                Log.d("결과", "성공: USDT-BTC: ${price}")
            }, {
                Log.d("실패", "업비트 시세 조회 실패")
            })
    }
}