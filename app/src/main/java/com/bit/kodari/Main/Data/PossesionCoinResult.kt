package com.bit.kodari.Main.Data

data class PossesionCoinResult(
    val userCoinIdx: Int,
    var upbitPrice: Double,
    var profit: Double,
    var coinImg: Any,
    val coinName: String,
    val priceAvg: Double,
    val status: String,
    val amount: Int,
    val symbol: String
    )