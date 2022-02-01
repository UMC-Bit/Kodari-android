package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateCoinPostResponse
import com.bit.kodari.Debate.Data.DebateCoinResponse
import com.bit.kodari.Debate.Data.DebatePostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DebateRetrofitInterface {
    @GET("/coins")              //토론장 코인 목록 조회
    fun getCoinsAll() : Call<DebateCoinResponse>

    @GET("/posts")
    fun getPostsAll() : Call<DebatePostResponse>

    //해당 코인에 대한 게시글들만 조회
    @GET("/posts/coin")
    fun getCoinPost(@Query("coinName") coinName: String) : Call<DebateCoinPostResponse>

}