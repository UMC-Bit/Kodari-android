package com.bit.kodari.Debate.Report.Retrofit

import com.bit.kodari.Debate.Report.ReportData.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReportRetrofitInterface {

    @POST("/reports/post")
    fun reportPost(@Header("X-ACCESS-TOKEN") jwt:String, @Body reportPostRequest: ReportPostRequest) : Call<ReportResponse>

    @POST("/reports/post/comment")
    fun reportComment(@Header("X-ACCESS-TOKEN") jwt:String,@Body reportCommentRequest: ReportCommentRequest ) : Call<ReportResponse>

    @POST("/reports/post/reply")
    fun reportRecomment(@Header("X-ACCESS-TOKEN") jwt:String, @Body reportRecommentRequest: ReportRecommentRequest) : Call<ReportResponse>

    @POST("/reports/post/user/report")
    fun reportUser(@Header("X-ACCESS-TOKEN") jwt:String, @Body reportUserRequest: ReportUserReqeust) : Call<ReportUserResponse>
}