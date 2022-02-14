package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinMgtInsquireResult(
    @SerializedName("userCoinIdx")var userCoinIdx: Int,
    @SerializedName("amount")var amount: Double,
    @SerializedName("coinIdx")var coinIdx: Int,
    @SerializedName("coinImg")var coinImg: String,
    @SerializedName("coinName")var coinName: String,
    @SerializedName("portIdx")var portIdx: Int,
    @SerializedName("priceAvg")var priceAvg: Double,
    @SerializedName("upbitPrice")var upbitPrice: Double,
    @SerializedName("profit")var profit: Double,
    @SerializedName("status")var status: String,
    @SerializedName("symbol")var symbol: String,
    @SerializedName("userIdx")var userIdx: Int
)