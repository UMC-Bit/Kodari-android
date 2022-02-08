package com.bit.kodari.Debate.PostData

import com.google.gson.annotations.SerializedName

data class DebatePostResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<DebatePostResult>
)