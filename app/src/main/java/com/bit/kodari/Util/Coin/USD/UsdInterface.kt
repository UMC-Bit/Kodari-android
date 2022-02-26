package com.bit.kodari.Util.Coin.USD

import retrofit2.Call
import retrofit2.http.GET

interface UsdInterface {
    @GET("/exchangeRates/get")
    fun getUsdExchangeRate() : Call<UsdResponse>
}