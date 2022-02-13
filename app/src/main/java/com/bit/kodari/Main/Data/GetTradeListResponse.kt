package com.bit.kodari.Main.Data

data class GetTradeListResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<GetTradeListResult>
)