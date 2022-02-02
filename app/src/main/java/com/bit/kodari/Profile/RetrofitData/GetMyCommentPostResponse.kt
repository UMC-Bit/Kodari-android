package com.bit.kodari.Profile.RetrofitData

data class GetMyCommentPostResponse(
    val comment_cnt: Int,
    val content: String,
    val dislike: Int,
    val like: Int,
    val nickName: String,
    val postIdx: Int,
    val profileImgUrl: String,
    val symbol: String,
    val time: String
)