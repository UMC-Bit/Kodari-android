package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse

interface PortManagementView {
    fun makePortSuccess(response: PsnCoinAddTradeResponse)
    fun makePortFailure(message:String)
}