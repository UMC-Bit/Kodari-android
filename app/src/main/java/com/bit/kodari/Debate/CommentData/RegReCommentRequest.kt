package com.bit.kodari.Debate.CommentData

data class RegReCommentRequest(
    val content: String,
    val postCommentIdx: Int,
    val userIdx: Int
)