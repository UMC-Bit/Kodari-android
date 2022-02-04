package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinMgtInsquireResult(
    @SerializedName("amount")val amount: Int,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("priceAvg")val priceAvg: Int,
    @SerializedName("status")val status: String,
    @SerializedName("userIdx")val userIdx: Int
)