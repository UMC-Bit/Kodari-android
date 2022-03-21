package com.bit.kodari.Debate.Report.ReportData

import com.google.gson.annotations.SerializedName

data class ReportPostRequest(
    @SerializedName("postIdx") val postIdx : Int,
    @SerializedName("reporter") val reporter : Int,
    @SerializedName("reason") val reason: String
)
