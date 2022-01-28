package com.bit.kodari.Main.Data

data class PossessionCoinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<PossesionCoinResult>
)