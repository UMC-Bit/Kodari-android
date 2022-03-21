package com.bit.kodari.Debate.Report.ReportData

import com.google.gson.annotations.SerializedName

data class ReportPostResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReportPostResult
)