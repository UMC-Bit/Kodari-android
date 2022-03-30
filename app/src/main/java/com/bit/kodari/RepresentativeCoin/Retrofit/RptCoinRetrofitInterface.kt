package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.Portfolio.Data.SearchCoinResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.*
import retrofit2.Call
import retrofit2.http.*

interface RptCoinRetrofitInterface {

    // 대표 코인 검색용 전체 코인
    @GET("/coins")
    fun getRptSearchCoinAll(): Call<RptCoinSearchResponse> // 응답받는 response 형식으로 맞춰주기


    //마켓별 코인 조회 -> 이걸로 전부 바꿔야함
    @GET("/coins/market")
    fun getMarketCoin(@Query("marketIdx") marketIdx:Int):Call<RptCoinSearchResponse>


    // 대표 코인 추가(등록)
    @POST("/represent/post")
    fun getRptCoinAdd(@Header("X-ACCESS-TOKEN") jwt :String,@Body rptCoinAddInfo: RptCoinAddInfo):Call<RptCoinAddResponse>

    // 대표 코인 조회
    @GET("/represent/{portIdx}")
    fun getRptCoinInquire(@Header("X-ACCESS-TOKEN") jwt :String, @Path("portIdx") portIdx : Int):Call<RptCoinMgtInsquireResponse>

    @DELETE("/represent/del/{representIdx}")
    fun deleteRptCoin(@Header("X-ACCESS-TOKEN") jwt :String, @Path("representIdx") representIdx : Int) : Call<DeleteRptCoinResponse>
}