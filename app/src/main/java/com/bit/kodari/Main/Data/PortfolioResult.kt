package com.bit.kodari.Main.Data

data class PortfolioResult (
    val accountIdx: Int,
    val accountName: String,
    val marketName: String,
    val portIdx: Int,
    val profitResultList: List<ProfitResult>,
    val property: Double,
    val representCoinList: List<RepresentCoinResult>,
    val totalProperty: Double,
    val userCoinList: List<PossesionCoinResult>,
    val userIdx: Int
)

data class PortIdxResult(
    val portIdx: Int
)