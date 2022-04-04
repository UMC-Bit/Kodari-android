package com.bit.kodari.Debate.Report.ReportData

import com.google.gson.annotations.SerializedName

data class ReportUserReqeust(
    @SerializedName("postIdx") val postIdx:Int,
    @SerializedName("reporter") val reporter:Int
)
