package com.bit.kodari.PossessionCoin.RetrofitData

data class PsnCoinAddResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: PsnCoinAddResult
)