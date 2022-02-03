package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.*
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
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
    fun writePost(@Header("X-ACCESS-TOKEN") jwt:String, @Body updatePostRequest: DebateWritePostRequest) : Call<DebateWritePostResponse>

    //해당 게시글 정보 가져오기 .
    @GET("/posts/post")
    fun selectPost(@Header("X-ACCESS-TOKEN") jwt:String ,@Query("postIdx") postIdx : Int) : Call<DebateSelectPostResponse>

    @GET("/app/users/get")
    fun getUserInfo(@Query("userIdx") userIdx: Int) : Call<GetProfileResponse>
}