package com.bit.kodari.Debate.CommentData

import com.google.gson.annotations.SerializedName

data class DeleteReCommentResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)