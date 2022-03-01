package com.bit.kodari.Portfolio.Data

import com.google.gson.annotations.SerializedName

data class SearchCoinResult(
    @SerializedName("coinIdx") val coinIdx: Int,
    @SerializedName("coinImg") val coinImg: String,
    @SerializedName("coinName") val coinName: String,
    @SerializedName("symbol") val symbol: String
)