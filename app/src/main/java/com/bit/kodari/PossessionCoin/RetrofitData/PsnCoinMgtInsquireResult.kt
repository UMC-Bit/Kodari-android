package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinMgtInsquireResult(
    @SerializedName("userCoinIdx")val userCoinIdx: Int,
    @SerializedName("amount")val amount: String,
    @SerializedName("coinIdx")val coinIdx: Int,
    @SerializedName("coinImg")val coinImg: String,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("portIdx")val portIdx: Int,
    @SerializedName("priceAvg")val priceAvg: String,
    @SerializedName("status")val status: String,
    @SerializedName("symbol")val symbol: String,
    @SerializedName("userIdx")val userIdx: Int
)