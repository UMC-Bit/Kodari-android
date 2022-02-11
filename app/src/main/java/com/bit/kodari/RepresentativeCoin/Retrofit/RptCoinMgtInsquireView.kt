package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse

interface RptCoinMgtInsquireView {
    fun rptCoinInsquireSuccess(response: RptCoinMgtInsquireResponse)
    fun rptCoinInsquireFailure(message:String)
}