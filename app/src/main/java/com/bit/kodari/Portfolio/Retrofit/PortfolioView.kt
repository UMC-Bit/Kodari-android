package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.Main.Data.PortfolioResponse

interface PortfolioView {
    fun portfolioSuccess(resp: PortfolioResponse)
    fun portfolioFailure(message: String)
}