package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinAddResult(
    @SerializedName("coinIdx") val coinIdx: Int,
    @SerializedName("portIdx") val portIdx: Int,
    @SerializedName("representIdx") val representIdx: Int
)