package com.bit.kodari.PossessionCoin.RetrofitData

data class PsnCoinAddTradeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: PsnCoinAddTradeResult
)