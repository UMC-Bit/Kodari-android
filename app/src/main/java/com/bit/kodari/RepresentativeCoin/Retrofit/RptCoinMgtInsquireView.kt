package com.bit.kodari.RepresentativeCoin.Retrofit

import com.bit.kodari.RepresentativeCoin.RetrofitData.DeleteRptCoinResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse

interface RptCoinMgtInsquireView {
    fun rptCoinInsquireSuccess(response: RptCoinMgtInsquireResponse)
    fun rptCoinInsquireFailure(message:String)
    fun deleteRptCoinSuccess(response:DeleteRptCoinResponse)
    fun deleteRptCoinFailure(message: String)
}