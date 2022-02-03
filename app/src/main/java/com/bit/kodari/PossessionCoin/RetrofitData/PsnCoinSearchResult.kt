package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinSearchResult(
    @SerializedName("coinIdx")val coinIdx: Int,
    @SerializedName("coinImg")val coinImg: String,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("symbol")val symbol: String
)