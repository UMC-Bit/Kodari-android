package com.bit.kodari.Util.Upbit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface UpbitAPI {
    @GET("v1/ticker")
    fun getCurrentPrice(
        @Header("Accept") accept: String,
        @Query("markets") markets: String
    ): Call<List<UpbitPrice>>
}