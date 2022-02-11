package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class PortfolioResult (
    @SerializedName("accountIdx") val accountIdx: Int,
    @SerializedName("accountName") val accountName: String,
    @SerializedName("marketName") val marketName: String,
    @SerializedName("portIdx") val portIdx: Int,
    @SerializedName("profitResultList") val profitResultList: List<ProfitResult>,
    @SerializedName("property") val property: Double,
    @SerializedName("representCoinList") val representCoinList: List<RepresentCoinResult>,
    @SerializedName("totalProperty") val totalProperty: Double,
    @SerializedName("userCoinList") val userCoinList: List<PossesionCoinResult>,
    @SerializedName("userIdx")  val userIdx: Int
)

data class PortIdxResult(
    @SerializedName("portIdx") val portIdx: Int
)