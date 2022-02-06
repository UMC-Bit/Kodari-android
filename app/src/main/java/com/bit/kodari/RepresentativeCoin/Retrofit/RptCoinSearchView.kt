package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse

interface RptCoinSearchView {
    fun getCoinsAllSuccess(response: RptCoinSearchResponse)
    fun getCoinsAllFailure(message:String)
}