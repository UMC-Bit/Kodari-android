package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinMgtInsquireResult(
    @SerializedName("coinImg")val coinImg: String,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("portIdx")val portIdx: Int,
    @SerializedName("symbol")val symbol: String,
)