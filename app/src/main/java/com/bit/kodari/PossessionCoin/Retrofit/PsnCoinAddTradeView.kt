package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse

interface PsnCoinAddTradeView {
    fun psnCoinAddTradeSuccess(response: PsnCoinAddTradeResponse)
    fun psnCoinAddTradeFailure(message:String)
}