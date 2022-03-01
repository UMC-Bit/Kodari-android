package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinMgtInsquireResult(
    @SerializedName("representIdx") var representIdx:Int,
    @SerializedName("coinIdx") var coinIdx: Int,
    @SerializedName("coinImg")var coinImg: String,
    @SerializedName("coinName")var coinName: String,
    @SerializedName("portIdx")var portIdx: Int,
    @SerializedName("symbol")var symbol: String,
    @SerializedName("status") var status:String,
    @SerializedName("upbitPrice") var upbitPrice: Double,
    @SerializedName("binancePrice") var binancePrice:Double,
    @SerializedName("kimchi") var kimchi:Double,
    @SerializedName("isChecked") var isChecked:Boolean
)