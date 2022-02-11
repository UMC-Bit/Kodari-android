package com.bit.kodari.Portfolio.Data

data class PostAccountResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: PostAccountResult
)