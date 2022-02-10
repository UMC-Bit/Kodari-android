package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddInfo
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface RptCoinRetrofitInterface {

    // 대표 코인 검색용 전체 코인
    @GET("/coins")
    fun getRptSearchCoinAll(): Call<RptCoinSearchResponse> // 응답받는 response 형식으로 맞춰주기

    // 대표 코인 추가(등록)
    @POST("/represent/post")
    fun getRptCoinAdd(@Body rptCoinAddInfo: RptCoinAddInfo):Call<RptCoinAddResponse>

    // 대표 코인 조회
    @GET("/represent/{portIdx}")
    fun getRptCoinInquire(@Header("X-ACCESS-TOKEN") jwt :String, @Path("portIdx") portIdx : Int):Call<RptCoinMgtInsquireResponse>
}