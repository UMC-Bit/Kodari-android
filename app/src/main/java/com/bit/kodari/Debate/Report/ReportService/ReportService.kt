package com.bit.kodari.Debate.Report.ReportService

import android.util.Log
import com.bit.kodari.Debate.Report.ReportData.*
import com.bit.kodari.Debate.Report.Retrofit.ReportPostView
import com.bit.kodari.Debate.Report.Retrofit.ReportRetrofitInterface
import com.bit.kodari.Debate.Report.Retrofit.ReportUserView
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportService {

    private lateinit var reportPostView: ReportPostView
    private lateinit var reportUserView : ReportUserView

    fun setReportPostView(reportPostView: ReportPostView) {
        this.reportPostView = reportPostView
    }

    fun setReportUserView(reportUserView: ReportUserView){
        this.reportUserView = reportUserView
    }

    fun reportPost(reportPostRequest: ReportPostRequest) {
        val reportService = getRetorfit().create(ReportRetrofitInterface::class.java)
        reportService.reportPost(getJwt()!!, reportPostRequest)
            .enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
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

    //유저 신고 기능
    fun reportUser(reportUserReqeust: ReportUserReqeust){
        val reportService = getRetorfit().create(ReportRetrofitInterface::class.java)
        reportService.reportUser(getJwt()!! , reportUserReqeust).enqueue(object : Callback<ReportUserResponse>{
            override fun onResponse(
                call: Call<ReportUserResponse>,
                response: Response<ReportUserResponse>
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000->{
                        //성공
                        reportUserView.getReportUserSuccess(resp)
                    } else->{
                        //실패
                        reportUserView.getReportUserFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ReportUserResponse>, t: Throwable) {
                reportUserView.getReportUserFailure(t.toString())
            }
        })

    }

}