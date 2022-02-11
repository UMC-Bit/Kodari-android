package com.bit.kodari.RepresentativeCoin.Service

import android.util.Log
import com.MyApplicationClass
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinMgtInsquireView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinRetrofitInterface
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinAddView
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinMgtInsquireView
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinRetrofitInterface
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinSearchView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddInfo
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class RptCoinService {
    private lateinit var rptCoinSearchView: RptCoinSearchView
    private lateinit var rptCoinAddView: RptCoinAddView
    private lateinit var rptCoinMgtInsquireView: RptCoinMgtInsquireView

    fun setRptCoinSearchView(rptCoinSearchView: RptCoinSearchView){
        this.rptCoinSearchView = rptCoinSearchView
    }

    fun setRptCoinAddView(rptCoinAddView: RptCoinAddView){
        this.rptCoinAddView=rptCoinAddView
    }

    fun setRptCoinMgtInsquireView(rptCoinMgtInsquireView: RptCoinMgtInsquireView){
        this.rptCoinMgtInsquireView=rptCoinMgtInsquireView
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

    //대표 코인 추가
    fun getRptCoinAdd(rptCoinAddInfo: RptCoinAddInfo){
        val rptCoinService= getRetorfit().create(RptCoinRetrofitInterface::class.java)

        rptCoinService.getRptCoinAdd(rptCoinAddInfo).enqueue(object : Callback<RptCoinAddResponse>{
            override fun onResponse(
                call: Call<RptCoinAddResponse>,
                response: Response<RptCoinAddResponse>
            ) {
                Log.d("rptCoinAddAllSuccess", "대표 코인 추가 성공")
                rptCoinAddView.rptCoinAddAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RptCoinAddResponse>, t: Throwable) {

            }

        })
    }

    //대표 코인 조회
    fun getRptCoinMgtInsquire()
    {
        val rptCoinService = getRetorfit().create(RptCoinRetrofitInterface::class.java)

        rptCoinService.getRptCoinInquire(getJwt()!! , MyApplicationClass.myPortIdx).enqueue(object : Callback<RptCoinMgtInsquireResponse>{
            override fun onResponse(
                call: Call<RptCoinMgtInsquireResponse>,
                response: Response<RptCoinMgtInsquireResponse>
            ) {
                Log.d("insquire success", "대표 코인 조회 성공")
                rptCoinMgtInsquireView.rptCoinInsquireSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RptCoinMgtInsquireResponse>, t: Throwable) {
                Log.d("insquire failure", "대표 코인 조회 실패")
                rptCoinMgtInsquireView.rptCoinInsquireFailure("${t}")
            }

        })
    }
    //대표 코인 선택 해서 삭제 , 여러개 추가 ->모두 구현해야함

}