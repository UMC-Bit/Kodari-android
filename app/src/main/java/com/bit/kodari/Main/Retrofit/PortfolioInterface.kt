package com.bit.kodari.Main.Retrofit

import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface PortfolioInterface {
    // userId로 portIdx 받아오기
    @GET("portfolio/getAll/{userIdx}")
    fun getAllPortfolio(
    @Path("userIdx") userIdx: Int
    ): Call<PortIdxResponse>

    // 포트폴리오 단일 조회
    @GET("portfolio/all/{portIdx}")
    fun getPortfolioByPortIdx(
        @Path("portIdx") portIdx: Int
    ): Call<PortfolioResponse>
}