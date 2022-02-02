package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse

interface PsnCoinAddView {

    fun psnCoinAddSuccess(response: PsnCoinAddResponse)
    fun psnCoinAddFailure(message:String)
}