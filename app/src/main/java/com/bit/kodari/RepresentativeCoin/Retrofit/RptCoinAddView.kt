package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddResponse

interface RptCoinAddView {
    fun rptCoinAddAllSuccess(response: RptCoinAddResponse)
    fun rptCoinAddAllFailure(message:String)
}