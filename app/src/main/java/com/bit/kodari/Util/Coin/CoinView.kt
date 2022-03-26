package com.bit.kodari.Util.Coin

import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

interface CoinView {
    fun marketPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>)
    fun marketFirstPriceSuccess(userCoinList: ArrayList<PossesionCoinResult>, representCoinList: ArrayList<RepresentCoinResult>)
    fun binancePriceSuccess(upbitCoinPriceMap: HashMap<String, Double>)
    fun coinPriceFailure(message: String)
}
