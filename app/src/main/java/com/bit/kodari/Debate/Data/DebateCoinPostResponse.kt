package com.bit.kodari.Debate.Data

data class DebateCoinPostResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<DebateCoinPostResult>
)