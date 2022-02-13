package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinMgtInsquireResult(
    @SerializedName("representIdx") val representIdx:Int,
    @SerializedName("coinIdx") val coinIdx: Int,
    @SerializedName("coinImg")val coinImg: String,
    @SerializedName("coinName")val coinName: String,
    @SerializedName("portIdx")val portIdx: Int,
    @SerializedName("symbol")val symbol: String,
    @SerializedName("status") val status:String,
    @SerializedName("upbitPrice") val upbitPrice: Double,
    @SerializedName("binancePrice") val binancePrice:Double,
    @SerializedName("isChecked") var isChecked:Boolean
)