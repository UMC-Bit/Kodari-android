package com.bit.kodari.Util.Coin

import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

interface CoinView {
    fun coinPriceSuccess(coinPriceMap: HashMap<String, Double>)
    fun coinPriceFailure(message: String)
}