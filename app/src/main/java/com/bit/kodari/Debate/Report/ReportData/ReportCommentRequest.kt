package com.bit.kodari.Debate.Report.ReportData

import com.google.gson.annotations.SerializedName

data class ReportCommentRequest(
    @SerializedName("postCommentIdx") var postCommentIdx : Int,
    @SerializedName("reporter") var reporter : Int,
    @SerializedName("reason") val reason: String
)
