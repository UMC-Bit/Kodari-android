package com.bit.kodari.Profile.RetrofitData

data class GetMyCommentResult(
    val content: String,
    val postList: List<GetMyCommentPostResponse>,
    val time: String
)