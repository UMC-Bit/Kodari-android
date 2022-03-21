package com.bit.kodari.Debate.Report.Retrofit

import com.bit.kodari.Debate.Report.ReportData.ReportPostResponse

interface ReportPostView {
    fun getReportPostSuccess(response: ReportPostResponse)
    fun getReportPostFailure(message:String)
}