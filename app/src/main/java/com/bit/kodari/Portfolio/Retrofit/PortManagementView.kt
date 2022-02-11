package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse

interface PortManagementView {
    fun makePortSuccess(response:PsnCoinAddResponse)
    fun makePortFailure(message:String)
}