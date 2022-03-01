package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinAddInfo( // 보낼 객체
    @SerializedName("userIdx") var userIdx:Int,
    @SerializedName("coinIdx") var coinIdx:Int,
    @SerializedName("accountIdx") var accountIdx:Int,
    @SerializedName("priceAvg") var priceAvg:String,
    @SerializedName("amount") var amount:String
)