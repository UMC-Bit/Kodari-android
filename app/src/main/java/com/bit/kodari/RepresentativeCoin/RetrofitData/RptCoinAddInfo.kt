package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinAddInfo(
    @SerializedName("portIdx") var portIdx: Int,
    @SerializedName("coinIdx") var coinIdx: Int
)
