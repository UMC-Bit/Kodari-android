package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import retrofit2.Call
import retrofit2.http.GET

interface RptCoinRetrofitInterface {
    @GET("/coins")
    fun getRptSearchCoinAll(): Call<RptCoinSearchResponse> // 응답받는 response 형식으로 맞춰주기
}