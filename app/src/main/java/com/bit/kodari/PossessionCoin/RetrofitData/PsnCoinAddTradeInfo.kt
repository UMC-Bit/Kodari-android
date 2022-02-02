package com.bit.kodari.PossessionCoin.RetrofitData

// BODY 부분(하얀색)- 주었을 때 데이터 클래스 (post방식 request)
data class PsnCoinAddTradeInfo(
    var portIdx : String,
    var coinIdx : String,
    var price : String,
    var amount : String,
    var fee : String,
    var category : String,
    var memo : String,
    var date: String
)