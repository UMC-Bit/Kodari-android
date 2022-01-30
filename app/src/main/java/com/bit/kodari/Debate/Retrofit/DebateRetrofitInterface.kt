package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateCoinResponse
import com.bit.kodari.Debate.Data.DebatePostResponse
import retrofit2.Call
import retrofit2.http.GET

interface DebateRetrofitInterface {
    @GET("/coins")              //토론장 코인 목록 조회
    fun getCoinsAll() : Call<DebateCoinResponse>

    @GET("/posts")
    fun getPostsAll() : Call<DebatePostResponse>

}