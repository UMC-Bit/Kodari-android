package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse

interface PsnCoinMgtInsquireView {

    fun psnCoinInsquireSuccess(response: PsnCoinMgtInsquireResponse)
    fun psnCoinInsquireFailure(message:String)
}