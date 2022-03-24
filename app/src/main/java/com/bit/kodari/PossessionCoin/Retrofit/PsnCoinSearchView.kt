package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResponse

interface PsnCoinSearchView {

    fun getCoinsAllSuccess(response: PsnCoinSearchResponse)
    fun getCoinsAllFailure(message:String)
    fun getMarketCoinSuccess(response: PsnCoinSearchResponse)
    fun getMarketCoinFailure(message: String)
}