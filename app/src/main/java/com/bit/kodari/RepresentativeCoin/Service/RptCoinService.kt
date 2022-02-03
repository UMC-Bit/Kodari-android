package com.bit.kodari.RepresentativeCoin.Service

import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinRetrofitInterface
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinSearchView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RptCoinService {
    private lateinit var rptCoinSearchView: RptCoinSearchView

    fun setRptCoinSearchView(rptCoinSearchView: RptCoinSearchView){
        this.rptCoinSearchView = rptCoinSearchView
    }

    // 전체 코인 검색 창에서 모든 코인 목록 받아오기
    fun getCoinsAll(){
        val rptCoinService = getRetorfit().create(RptCoinRetrofitInterface::class.java)

        rptCoinService.getRptSearchCoinAll().enqueue(object : Callback<RptCoinSearchResponse> {
            override fun onResponse(
                call: Call<RptCoinSearchResponse>,
                response: Response<RptCoinSearchResponse>
            ) {
                rptCoinSearchView.getCoinsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RptCoinSearchResponse>, t: Throwable) {
                rptCoinSearchView.getCoinsAllFailure("t")
            }
        })
    }
}