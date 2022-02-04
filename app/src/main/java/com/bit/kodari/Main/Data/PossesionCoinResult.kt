package com.bit.kodari.Main.Data

data class PossesionCoinResult(
    val userCoinIdx: Int,
    val coinName: String,
    val priceAvg: Double,
    val status: String,
    val amount: Int,
    val symbol: String
    )