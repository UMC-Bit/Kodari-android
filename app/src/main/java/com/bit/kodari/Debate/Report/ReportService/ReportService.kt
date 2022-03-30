package com.bit.kodari.Debate.Report.ReportService

import android.util.Log
import com.bit.kodari.Debate.Report.ReportData.ReportCommentRequest
import com.bit.kodari.Debate.Report.ReportData.ReportPostRequest
import com.bit.kodari.Debate.Report.ReportData.ReportRecommentRequest
import com.bit.kodari.Debate.Report.ReportData.ReportResponse
import com.bit.kodari.Debate.Report.Retrofit.ReportPostView
import com.bit.kodari.Debate.Report.Retrofit.ReportRetrofitInterface
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportService {

    private lateinit var reportPostView: ReportPostView

    fun setReportPostView(reportPostView: ReportPostView) {
        this.reportPostView = reportPostView
    }

    fun reportPost(reportPostRequest: ReportPostRequest) {
        val reportService = getRetorfit().create(ReportRetrofitInterface::class.java)
        reportService.reportPost(getJwt()!!, reportPostRequest)
            .enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                    Log.d("report", "${response.body()}")
                    val resp = response.body()!!

                    when (resp.code) {
                        1005,1006,4088 -> {
                            reportPostView.getReportPostSuccess(resp)
                        }
                        else -> {
                            reportPostView.getReportPostFailure(resp.message)
                        }
                    }

                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    reportPostView.getReportPostFailure("${t}")
                }
            })
    }

    fun reportComment(reportCommentRequest: ReportCommentRequest){
        val reportService = getRetorfit().create(ReportRetrofitInterface::class.java)
        reportService.reportComment(getJwt()!!, reportCommentRequest).enqueue(object : Callback<ReportResponse>{
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                Log.d("reportComment", "${response.body()}")
                val resp = response.body()!!
                when (resp.code) {
                    1007,1008,4088 -> {
                        reportPostView.getReportCommentSuccess(resp)
                    }
                    else -> {
                        reportPostView.getReportCommentFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                reportPostView.getReportCommentFailure("${t}")
            }
        })
    }

    fun reportRecomment(reportRecommentRequest: ReportRecommentRequest){
        val reportService = getRetorfit().create(ReportRetrofitInterface::class.java)
        reportService.reportRecomment(getJwt()!!, reportRecommentRequest).enqueue(object : Callback<ReportResponse>{
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                Log.d("reportRecomment", "${response.body()}")
                val resp = response.body()!!
                when (resp.code) {
                    1007,1008,4088 -> {
                        reportPostView.getReportRecommentSuccess(resp)
                    }
                    else -> {
                        reportPostView.getReportRecommentFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                reportPostView.getReportRecommentFailure("${t}")
            }
        })
    }

}