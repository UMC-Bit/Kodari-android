package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Portfolio.Data.SearchCoinResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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

    // 코인 목록 조회
    @GET("/coins")
    fun getSearchCoinAll():Call<SearchCoinResponse>
}