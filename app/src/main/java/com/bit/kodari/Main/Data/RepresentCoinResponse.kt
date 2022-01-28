package com.bit.kodari.Main.Data

data class RepresentCoinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<RepresentCoinResult>
)