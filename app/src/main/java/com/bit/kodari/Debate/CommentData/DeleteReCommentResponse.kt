package com.bit.kodari.Debate.CommentData

data class DeleteReCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)