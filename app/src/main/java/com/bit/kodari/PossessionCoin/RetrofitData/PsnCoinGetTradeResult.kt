package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinGetTradeResult(
    @SerializedName("amount") val amount: Int,
    @SerializedName("category") val category: String,
    @SerializedName("coinName") val coinName: String,
    @SerializedName("date") val date: String,
    @SerializedName("fee") val fee: Double,
    @SerializedName("memo") val memo: String,
    @SerializedName("price") val price: Int,
    @SerializedName("status") val status: String,
    @SerializedName("tradeIdx") val tradeIdx: Int
)