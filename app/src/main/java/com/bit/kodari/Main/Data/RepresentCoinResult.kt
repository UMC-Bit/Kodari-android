package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class RepresentCoinResult(
    @SerializedName("coinIdx") var coinIdx: Int,
    @SerializedName("binancePrice") var binancePrice: Double,
    @SerializedName("upbitPrice") var upbitPrice: Double,
    @SerializedName("kimchi") var kimchi: Double,
    @SerializedName("coinImg") var coinImg: Any,
    @SerializedName("coinName") var coinName: String,
    @SerializedName("portIdx") var portIdx: Int,
    @SerializedName("representIdx") var representIdx: Int,
    @SerializedName("status") var status: String,
    @SerializedName("symbol") var symbol: String,
    var isChecked: Boolean,
    var change: Double
)
