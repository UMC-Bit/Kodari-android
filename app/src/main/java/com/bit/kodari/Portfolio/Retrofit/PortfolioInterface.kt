package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Portfolio.Data.*
import retrofit2.Call
import retrofit2.http.*

public interface PortfolioInterface {

    // userId로 portIdx 받아오기
    @GET("portfolio/getAll/{userIdx}")
    fun getAllPortfolio(
    @Path("userIdx") userIdx: Int,
    @Header("x-access-token") jwt: String?
    ): Call<PortIdxResponse>

    // 포트폴리오 단일 조회
    @GET("portfolio/all/{portIdx}")
    fun getPortfolioByPortIdx(
        @Path("portIdx") portIdx: Int,
        @Header("x-access-token") jwt: String?
    ): Call<PortfolioResponse>

    // 코인 목록 조회 , 마켓 상관없이 모든 코인 조회
    @GET("/coins")
    fun getSearchCoinAll():Call<SearchCoinResponse>

    //마켓별 코인 조회 -> 이걸로 전부 바꿔야함
    @GET("/coins/market")
    fun getSearchMarketCoin(@Query("marketIdx") marketIdx:Int):Call<SearchCoinResponse>

    //계좌 등록 API
    @POST("/account/post")
    fun postAccount(@Header("X-ACCESS-TOKEN") jwt:String , @Body postAccountRequest: PostAccountRequest) : Call<PostAccountResponse>

    //포토폴리오 생성 API
    @POST("/portfolio/post")
    fun postPortFolio(@Header("X-ACCESS-TOKEN") jwt:String , @Body postPortFolioRequest: PostPortFolioRequest) : Call<PostPortFolioResponse>

    //소유 코인 등록 API 새로만들어야하나 ? ?
}