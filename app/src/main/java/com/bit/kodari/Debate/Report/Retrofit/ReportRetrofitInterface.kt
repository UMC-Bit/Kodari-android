package com.bit.kodari.Debate.Report.Retrofit

import com.bit.kodari.Debate.Report.ReportData.ReportPostRequest
import com.bit.kodari.Debate.Report.ReportData.ReportPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReportRetrofitInterface {

    @POST("/reports/post")
    fun reportPost(@Header("X-ACCESS-TOKEN") jwt:String, @Body reportPostRequest: ReportPostRequest) : Call<ReportPostResponse>

}