package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PsnCoinRetrofitInterface {
    @POST("/post")
    fun getPsnCoinAdd(@Body psnCoinAddInfo: PsnCoinAddInfo):Call<PsnCoinAddResponse> // psncoinaddinfo 객체를 보냄
}