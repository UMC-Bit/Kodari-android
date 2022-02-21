package com.bit.kodari.PossessionCoin.RetrofitData

data class DeleteTradeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)