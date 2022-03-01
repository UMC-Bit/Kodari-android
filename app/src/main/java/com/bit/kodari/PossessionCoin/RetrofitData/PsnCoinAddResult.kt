package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinAddResult(
    @SerializedName("coinIdx") val coinIdx: Int,
    @SerializedName("userCoinIdx") val userCoinIdx: Int
)