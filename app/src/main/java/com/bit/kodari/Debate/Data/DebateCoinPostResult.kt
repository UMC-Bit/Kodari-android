package com.bit.kodari.Debate.Data

data class DebateCoinPostResult(
    val comment_cnt: Int,
    val content: String,
    val dislike: Int,
    val like: Int,
    val nickName: String,
    val profileImgUrl: String,
    val symbol: String,
    val time: String
)