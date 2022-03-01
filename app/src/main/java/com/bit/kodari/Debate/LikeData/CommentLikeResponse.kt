package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class CommentLikeResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CommentLikeResult
)