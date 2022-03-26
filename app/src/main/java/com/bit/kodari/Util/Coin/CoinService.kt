package com.bit.kodari.Util.Upbit

import android.util.Log
import com.amazonaws.util.XpathUtils
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Util.Coin.Bithumb.BithumbResponse
import com.bit.kodari.Util.Coin.CoinInterface
import com.bit.kodari.Util.Coin.CoinView
import com.bit.kodari.Util.Coin.CoinViewModel
import com.bit.kodari.Util.Coin.Upbit.UpbitResult
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
    private lateinit var viewModel: CoinViewModel
    private lateinit var userCoinList: ArrayList<PossesionCoinResult>
    private lateinit var representCoinList: ArrayList<RepresentCoinResult>

    fun setCoinView(coinView: CoinView) {
        this.coinView = coinView
    }
    fun setViewModel(viewModel: CoinViewModel) {
        this.viewModel = viewModel
    }
    // MARKET BASE URL
    private val BASE_URL_UPBIT_API = "https://api.upbit.com/"
    private val BASE_URL_BINANCE_API = "https://api.binance.com"
    private val BASE_URL_BITHUMB_API = "https://api.bithumb.com"

    /*
        업비트 현재 코인 시세 받아오기
        소유코인, 대표코인
     */
    fun getUpbitCurrentPrice(
        userCoinList_: ArrayList<PossesionCoinResult>,
        representCoinList_: ArrayList<RepresentCoinResult>
    ) {
        userCoinList = userCoinList_
        representCoinList = representCoinList_
        // Retrofit 초기 설정
        val upbitRetrofit = Retrofit.Builder()         // upbit
            .baseUrl(BASE_URL_UPBIT_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val upbitApi = upbitRetrofit.create(CoinInterface::class.java)
        // 업비트 유저 코인시세 받아오기
        for (i in 0 until userCoinList.size) {
            upbitApi.getCurrentUpbitPrice("application/json", "KRW-" + userCoinList[i].symbol)
                .enqueue(
                    object: Callback<List<UpbitResult>>{
                        override fun onResponse(
                            call: Call<List<UpbitResult>>,
                            response: Response<List<UpbitResult>>
                        ) {
                            val price = response.body()?.get(0)?.trade_price
                            if(userCoinList.size <= i) // 늦게 응답 받아올 경우 예외처리
                                return
                            if (price != null) {
                                userCoinList[i].marketPrice = price
                            } else {
                                userCoinList[i].marketPrice = 0.0
                            }
                            viewModel.getUpdateUserCoin(userCoinList, i)
                            Log.d("업비트 API", "성공: ${price}")
                        }
                        override fun onFailure(call: Call<List<UpbitResult>>, t: Throwable) {
                            Log.d("업비트 API", "실패")
                        }
                    }
                )
        }

        // 업비트 대표코인 시세 받아오기
        for (i in 0 until representCoinList.size) {
            upbitApi.getCurrentUpbitPrice("application/json", "KRW-" + representCoinList[i].symbol)
                .enqueue(
                    object: Callback<List<UpbitResult>>{
                        override fun onResponse(
                            call: Call<List<UpbitResult>>,
                            response: Response<List<UpbitResult>>
                        ) {
                            val price = response.body()?.get(0)?.trade_price
                            if(representCoinList.size <= i) // 늦게 응답 받아올 경우 예외처리
                                return
                            if (price != null) {
                                representCoinList[i].marketPrice = price
                            } else {
                                representCoinList[i].marketPrice = 0.0
                            }
                            viewModel.getUpdateRepresentCoin(representCoinList, i)
                            Log.d("업비트 API", "성공: ${price}")
                        }
                        override fun onFailure(call: Call<List<UpbitResult>>, t: Throwable) {
                            Log.d("업비트 API", "실패")
                        }
                    }
                )
        }
    }
    /*
            빗썸 현재 코인 시세 받아오기
            소유코인, 대표코인
         */
    fun getBithumbCurrentPrice(
        userCoinList_: ArrayList<PossesionCoinResult>,
        representCoinList_: ArrayList<RepresentCoinResult>
    ) {
        userCoinList = userCoinList_
        representCoinList = representCoinList_
        // Retrofit 초기 설정
        val bithumbRetrofit = Retrofit.Builder()         // bithumb
            .baseUrl(BASE_URL_BITHUMB_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val bithumbApi = bithumbRetrofit.create(CoinInterface::class.java)
        // 빗썸 소유코인 시세 받아오기
        for (i in 0 until userCoinList.size) {
            bithumbApi.getCurrentBithumbPrice(userCoinList[i].symbol + "_KRW")
                .enqueue(
                    object: Callback<BithumbResponse>{
                        override fun onResponse(
                            call: Call<BithumbResponse>,
                            response: Response<BithumbResponse>
                        ) {
                            if(userCoinList.size <= i) // 늦게 응답 받아올 경우 예외처리
                                return
                            val price = response.body()?.data?.closing_price
                            if (price != null) {
                                userCoinList[i].marketPrice = price.toDouble()
                            } else {
                                userCoinList[i].marketPrice = 0.0
                            }
                            viewModel.getUpdateUserCoin(userCoinList, i)
                            Log.d("빗썸 API", "성공: ${price}")
                        }

                        override fun onFailure(call: Call<BithumbResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    }
                )
        }
        // 빗썸 대표코인 시세 받아오기
        for (i in 0 until representCoinList.size) {
            bithumbApi.getCurrentBithumbPrice(representCoinList[i].symbol + "_KRW")
                .enqueue(
                    object: Callback<BithumbResponse>{
                        override fun onResponse(
                            call: Call<BithumbResponse>,
                            response: Response<BithumbResponse>
                        ) {
                            if(representCoinList.size <= i) // 늦게 응답 받아올 경우 예외처리
                                return
                            val price = response.body()?.data?.closing_price
                            if (price != null) {
                                representCoinList[i].marketPrice = price.toDouble()
                            } else {
                                representCoinList[i].marketPrice = 0.0
                            }
                            viewModel.getUpdateRepresentCoin(representCoinList, i)
                            Log.d("빗썸 API", "성공: ${price}")
                        }
                        override fun onFailure(call: Call<BithumbResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    }
                )
        }
    }
}


