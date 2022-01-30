package com.bit.kodari.Debate.Data

data class DebateCoinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<DebateCoinResult>
)