package com.bit.kodari.Main.Data

data class RepresentCoinResult(
    var coinIdx: Int,
    var binancePrice: Double,
    var upbitPrice: Double,
    var kimchi: Double,
    var coinImg: Any,
    var coinName: String,
    var portIdx: Int,
    var representIdx: Int,
    var status: String,
    var symbol: String
)