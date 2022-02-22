package com.bit.kodari.Util.Coin


import com.bit.kodari.Util.Coin.Binance.BinanceResult
import com.bit.kodari.Util.Coin.Upbit.UpbitPrice
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinInterface {
    // 업비트
    @GET("v1/ticker")
    fun getCurrentUpbitPrice(
        @Header("Accept") accept: String,
        @Query("markets") markets: String
    ): Single<List<UpbitPrice>>

    // 바이낸스
    @GET("/api/v3/ticker/price")
    fun getCurrentBinancePrice(
    ): Single<List<BinanceResult>>
}