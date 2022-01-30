package com.bit.kodari.Debate.Data

import com.google.gson.annotations.SerializedName

data class DebateUpdatePostRequest(
    @SerializedName("cointIdx") val coinIdx: Int,
    @SerializedName("userIdx") val userIdx: Int,
    @SerializedName("content") val content: String,
)