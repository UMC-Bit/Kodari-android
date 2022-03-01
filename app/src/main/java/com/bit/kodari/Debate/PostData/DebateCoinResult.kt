package com.bit.kodari.Debate.PostData

import com.google.gson.annotations.SerializedName

data class DebateCoinResult(
    @SerializedName("coinIdx") val coinIdx:Int,
    @SerializedName("coinImg") val coinImg: String,
    @SerializedName("coinName") val coinName: String,
    @SerializedName("symbol") val symbol: String
)