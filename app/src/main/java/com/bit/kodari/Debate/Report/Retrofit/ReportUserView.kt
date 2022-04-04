package com.bit.kodari.Debate.Report.Retrofit

import com.bit.kodari.Debate.Report.ReportData.ReportResponse
import com.bit.kodari.Debate.Report.ReportData.ReportUserReqeust
import com.bit.kodari.Debate.Report.ReportData.ReportUserResponse

interface ReportUserView {
    fun getReportUserSuccess(response: ReportUserResponse)
    fun getReportUserFailure(message:String)
}