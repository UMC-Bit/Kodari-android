package com.bit.kodari.RepresentativeCoin.RetrofitData

data class DeleteRptCoinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)