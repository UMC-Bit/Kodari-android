package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.*
import retrofit2.Call
import retrofit2.http.*

interface DebateRetrofitInterface {
    @GET("/coins")              //토론장 코인 목록 조회
    fun getCoinsAll() : Call<DebateCoinResponse>

    @GET("/posts")
    fun getPostsAll() : Call<DebatePostResponse>

    //해당 코인에 대한 게시글들만 조회
    @GET("/posts/coin")
    fun getCoinPost(@Query("coinName") coinName: String) : Call<DebateCoinPostResponse>

    @POST("/posts/register")
    fun updatePost(@Body updatePostRequest: DebateUpdatePostRequest) : Call<DebateUpdatePostResponse>

}