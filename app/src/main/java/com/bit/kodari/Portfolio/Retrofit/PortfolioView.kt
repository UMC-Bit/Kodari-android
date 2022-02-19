package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse

interface PortfolioView {
    fun getPortIdxSuccess(resp: PortIdxResponse)
    fun getPortIdxFailure(message: String)
    fun portfolioSuccess(resp: PortfolioResponse)
    fun portfolioFailure(message: String)
    fun getAccountProfit(currentSum: Double, sumBuyCoin: Double)// 계좌 수익률 조회
}