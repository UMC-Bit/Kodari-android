package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinSearchResult(
    @SerializedName("coinImg")val coinImg: Any,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("symbol")val symbol: String
)