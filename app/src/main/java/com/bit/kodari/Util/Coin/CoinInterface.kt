package com.bit.kodari.Util.Coin

import com.bit.kodari.Util.Binance.BinanceResult
import com.bit.kodari.Util.Upbit.UpbitPrice
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinInterface {
    // 업비트
    @GET("v1/ticker")
    fun getCurrentUpbitPrice(
        @Header("Accept") accept: String,
        @Query("markets") markets: String
    ): Call<List<UpbitPrice>>

    // 바이낸스
    @GET("/api/v3/ticker/price")
    fun getCurrentBinancePrice(
    ): Call<List<BinanceResult>>
}