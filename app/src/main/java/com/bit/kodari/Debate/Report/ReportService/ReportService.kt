package com.bit.kodari.Debate.Report.ReportService

import android.util.Log
import com.bit.kodari.Debate.Report.ReportData.ReportPostRequest
import com.bit.kodari.Debate.Report.ReportData.ReportPostResponse
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
            .enqueue(object : Callback<ReportPostResponse> {
                override fun onResponse(
                    call: Call<ReportPostResponse>,
                    response: Response<ReportPostResponse>
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

                override fun onFailure(call: Call<ReportPostResponse>, t: Throwable) {
                    reportPostView.getReportPostFailure("${t}")
                }
            })
    }

}