package com.bit.kodari.Util.Coin

import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

interface CoinView {
    fun upbitPriceSuccess(upbitCoinPriceMap: HashMap<String, Double>)
    fun binancePriceSuccess(upbitCoinPriceMap: HashMap<String, Double>)
    fun usdtPriceSuccess(usdtPrice: Int)
    fun coinPriceFailure(message: String)
}