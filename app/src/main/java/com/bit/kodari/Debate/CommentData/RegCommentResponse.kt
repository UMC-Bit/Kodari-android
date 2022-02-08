package com.bit.kodari.Debate.CommentData

data class RegCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: RegCommentResult
)