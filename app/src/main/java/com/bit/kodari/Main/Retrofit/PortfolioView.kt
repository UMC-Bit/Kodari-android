package com.bit.kodari.Main.Retrofit

import com.bit.kodari.Main.Data.PortfolioResponse

interface PortfolioView {
    fun portfolioSuccess(resp: PortfolioResponse)
    fun portfolioFailure(message: String)
}