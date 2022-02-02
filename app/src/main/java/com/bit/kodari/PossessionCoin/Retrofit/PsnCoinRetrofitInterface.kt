package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PsnCoinRetrofitInterface {
    // get방식은 받는거라서 따로 만들어서 보내줄 필요가 없지만 post형식은 body를 보내줘야 response가 온다.
    @POST("/userCoin/post")
    fun getPsnCoinAdd(@Body psnCoinAddInfo: PsnCoinAddInfo):Call<PsnCoinAddResponse> // psncoinaddinfo 객체를 보냄

    @GET("/coins")
    fun getPsnSearchCoinAll():Call<PsnCoinSearchResponse> // 응답받는 response 형식으로 맞춰주기
}