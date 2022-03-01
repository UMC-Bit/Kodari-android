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
import com.bit.kodari.RepresentativeCoin.RetrofitData.*
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
    fun getRptCoinAdd(addList: HashSet<Int>){
        val rptCoinService= getRetorfit().create(RptCoinRetrofitInterface::class.java)
        for(curIdx in addList){
            val rptCoinAddInfo = RptCoinAddInfo(MyApplicationClass.myPortIdx , curIdx)
            rptCoinService.getRptCoinAdd(getJwt()!!, rptCoinAddInfo).enqueue(object : Callback<RptCoinAddResponse>{
                override fun onResponse(
                    call: Call<RptCoinAddResponse>,
                    response: Response<RptCoinAddResponse>
                ) {
                    when(response.body()!!.code){
                      1000 -> {rptCoinAddView.rptCoinAddAllSuccess(response.body()!!)}
                      else -> {rptCoinAddView.rptCoinAddAllFailure(response.body()!!.message)}
                    }

                }
                override fun onFailure(call: Call<RptCoinAddResponse>, t: Throwable) {
                   Log.d("RptAdd" , t.toString())
                }

            })
        }

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
    fun deleteRptCoin(coinList : HashSet<Int>){
        val rptCoinService = getRetorfit().create(RptCoinRetrofitInterface::class.java)
        for(curCoinIdx in coinList){
            rptCoinService.deleteRptCoin(getJwt()!!,curCoinIdx).enqueue(object : Callback<DeleteRptCoinResponse>{
                override fun onResponse(
                    call: Call<DeleteRptCoinResponse>,
                    response: Response<DeleteRptCoinResponse>
                ) {
                    when(response.body()!!.code){
                      1000 -> {
                          rptCoinMgtInsquireView.deleteRptCoinSuccess(response.body()!!)
                      }
                      else -> {rptCoinMgtInsquireView.deleteRptCoinFailure(response.body()!!.message)}
                    }
                }

                override fun onFailure(call: Call<DeleteRptCoinResponse>, t: Throwable) {
                    rptCoinMgtInsquireView.deleteRptCoinFailure(t.toString())
                }
            })
        }

    }
}