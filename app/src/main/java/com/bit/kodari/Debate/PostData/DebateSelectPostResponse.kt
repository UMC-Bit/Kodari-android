package com.bit.kodari.Debate.PostData

import com.google.gson.annotations.SerializedName

data class DebateSelectPostResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: DebateSelectPostResult
)