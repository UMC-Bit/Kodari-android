package com.bit.kodari.Debate.CommentData

data class DeleteCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)