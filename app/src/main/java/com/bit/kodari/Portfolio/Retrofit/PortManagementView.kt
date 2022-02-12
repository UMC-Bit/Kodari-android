package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse

interface PortManagementView {
    fun makePortSuccess(response: PsnCoinAddResponse)
    fun makePortFailure(message:String)
}