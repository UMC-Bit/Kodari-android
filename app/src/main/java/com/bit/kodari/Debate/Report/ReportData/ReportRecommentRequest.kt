package com.bit.kodari.Debate.Report.ReportData

import com.google.gson.annotations.SerializedName

data class ReportRecommentRequest(
    @SerializedName("postReplyIdx") var postReplyIdx : Int,
    @SerializedName("reporter") var reporter : Int,
    @SerializedName("reason") val reason: String
)
