package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class PossesionCoinResult(
    @SerializedName("userCoinIdx") val userCoinIdx: Int,
    @SerializedName("marketPrice") var marketPrice: Double,
    @SerializedName("binancePrice") var binancePrice: Double,
    @SerializedName("kimchi") var kimchi: Double,
    @SerializedName("profit") var profit: Double,
    @SerializedName("profitRate") var profitRate: Double,
    @SerializedName("coinImg") var coinImg: String,
    @SerializedName("coinName") val coinName: String,
    @SerializedName("priceAvg") val priceAvg: Double,
    @SerializedName("status") val status: String,
    @SerializedName("amount")  val amount: Double,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("coinIdx") val coinIdx: Int,
    @SerializedName("twitter") val twitter:String,
    @SerializedName("change") var change:Double,
    var isChecked : Boolean
    )