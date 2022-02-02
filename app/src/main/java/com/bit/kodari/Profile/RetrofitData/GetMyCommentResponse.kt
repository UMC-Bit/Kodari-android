package com.bit.kodari.Profile.RetrofitData

data class GetMyCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetMyCommentResult>
)