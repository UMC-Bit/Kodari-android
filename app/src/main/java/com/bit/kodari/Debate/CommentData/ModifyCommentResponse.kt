package com.bit.kodari.Debate.CommentData

data class ModifyCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)