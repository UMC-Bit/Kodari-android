package com.bit.kodari.Debate.Report.Retrofit

import com.bit.kodari.Debate.Report.ReportData.ReportResponse

interface ReportPostView {
    fun getReportPostSuccess(response: ReportResponse)
    fun getReportPostFailure(message:String)
    fun getReportCommentSuccess(response: ReportResponse)
    fun getReportCommentFailure(message:String)
    fun getReportRecommentSuccess(response: ReportResponse)
    fun getReportRecommentFailure(message: String)
}