package com.bit.kodari.PossessionCoin.RetrofitData

data class PsnCoinAddInfo( // 보낼 객체
    var userIdx:Int,
    var coinIdx:Int,
    var accountIdx:Int,
    var priceAvg:String,
    var amount:String
)