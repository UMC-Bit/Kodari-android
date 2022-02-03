package com.bit.kodari.Util.BItfinex

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceInterface {
    @GET("/api/v3/ticker/price")
    fun getCurrentPrice(
        @Query("symbol") symbol: String
    ): Call<List<BinanceResult>>
}