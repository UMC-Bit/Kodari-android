package com.bit.kodari.Debate.Data

import com.google.gson.annotations.SerializedName

data class DebateCoinResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<DebateCoinResult>
)