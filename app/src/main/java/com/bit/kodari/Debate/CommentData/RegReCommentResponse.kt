package com.bit.kodari.Debate.CommentData

data class RegReCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: RegReCommentResult
)