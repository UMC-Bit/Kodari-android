package com.bit.kodari.Debate.CommentData

data class RegCommentRequest(
    val content: String,
    val postIdx: Int,
    val userIdx: Int
)