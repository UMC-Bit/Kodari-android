package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinSearchResult(
    @SerializedName("coinIdx")val coinIdx: Int,
    @SerializedName("coinImg")val coinImg: String,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("symbol")val symbol: String,
    @SerializedName("isCheck")var isCheck: Boolean
)