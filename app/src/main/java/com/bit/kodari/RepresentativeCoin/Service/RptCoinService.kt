package com.bit.kodari.RepresentativeCoin.Service

import android.util.Log
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinRetrofitInterface
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinAddView
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinRetrofitInterface
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinSearchView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddInfo
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class RptCoinService {
    private lateinit var rptCoinSearchView: RptCoinSearchView
    private lateinit var rptCoinAddView: RptCoinAddView

    fun setRptCoinSearchView(rptCoinSearchView: RptCoinSearchView){
        this.rptCoinSearchView = rptCoinSearchView
    }

    fun setRptCoinAddView(rptCoinAddView: RptCoinAddView){
        this.rptCoinAddView=rptCoinAddView
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

    fun getRptCoinAdd(rptCoinAddInfo: RptCoinAddInfo){
        val rptCoinService= getRetorfit().create(RptCoinRetrofitInterface::class.java)

        rptCoinService.getRptCoinAdd(rptCoinAddInfo).enqueue(object : Callback<RptCoinAddResponse>{
            override fun onResponse(
                call: Call<RptCoinAddResponse>,
                response: Response<RptCoinAddResponse>
            ) {
                Log.d("rptCoinAddAllSuccess", "소유 코인 추가 성공")
                rptCoinAddView.rptCoinAddAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RptCoinAddResponse>, t: Throwable) {

            }

        })
    }

//    fun getPsnCoinAdd(psnCoinAddInfo: PsnCoinAddInfo){
//        val psnCoinService= getRetorfit().create(PsnCoinRetrofitInterface::class.java)
//
//        psnCoinService.getPsnCoinAdd(psnCoinAddInfo).enqueue(object : Callback<PsnCoinAddResponse> {
//            override fun onResponse( // 통신 성공
//                call: Call<PsnCoinAddResponse>,
//                response: Response<PsnCoinAddResponse>
//            ) {
//                Log.d("psnCoinAddSuccess", "소유 코인 추가 성공")
//                psnCoinAddView.psnCoinAddSuccess(response.body()!!)
//            }
//
//            override fun onFailure(call: Call<PsnCoinAddResponse>, t: Throwable) { // 통신 실패
//                Log.d("psnCoinAddFailure", "소유 코인 추가 실패")
//                psnCoinAddView.psnCoinAddFailure("t")
//            }
//        })
//    }
}