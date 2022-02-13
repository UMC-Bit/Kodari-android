package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

// BODY 부분(하얀색)- 주었을 때 데이터 클래스 (post방식 request)
data class PsnCoinAddTradeInfo(
    @SerializedName("portIdx") var portIdx : Int,
    @SerializedName("coinIdx") var coinIdx : Int,
    @SerializedName("price") var price : String,
    @SerializedName("amount") var amount : String,            //Double이 아닌 String으로 넘겨도되나?
    @SerializedName("fee") var fee : Double,
    @SerializedName("category") var category : String,
    @SerializedName("memo") var memo : String,
    @SerializedName("date") var date: String
)