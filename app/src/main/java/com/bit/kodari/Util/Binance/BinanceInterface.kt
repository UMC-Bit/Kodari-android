package com.bit.kodari.Util.Binance

import retrofit2.Call
import retrofit2.http.GET

interface BinanceInterface {
    @GET("/api/v3/ticker/price")
    fun getCurrentPrice(
    ): Call<List<BinanceResult>>
}