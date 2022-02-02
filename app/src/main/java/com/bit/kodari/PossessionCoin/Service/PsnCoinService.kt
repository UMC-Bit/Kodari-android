package com.bit.kodari.PossessionCoin.Service

import android.util.Log
import com.bit.kodari.PossessionCoin.PossessionCoinAddFragment
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddTradeView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinRetrofitInterface
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinSearchView
import com.bit.kodari.PossessionCoin.RetrofitData.*
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 여기서 함수들을 호출
class PsnCoinService{ // PsnCoinService의 매개변수는 없어도 되나 toast 메시지를 위한 매개변수

    private lateinit var psnCoinSearchView: PsnCoinSearchView
    private lateinit var psnCoinAddView: PsnCoinAddView
    private lateinit var psnCoinAddTradeView: PsnCoinAddTradeView

    fun setPsnCoinSearchView(psnCoinSearchView: PsnCoinSearchView){
        this.psnCoinSearchView = psnCoinSearchView
    }

    fun setPsnCoinAddView(psnCoinAddView: PsnCoinAddView){
        this.psnCoinAddView=psnCoinAddView
    }

    fun setPsnCoinAddTradeView(psnCoinAddTradeView: PossessionCoinAddFragment){
        this.psnCoinAddTradeView=psnCoinAddTradeView
    }



    // 전체 코인 검색 창에서 모든 코인 목록 받아오기
    fun getCoinsAll(){
        val psnCoinService = getRetorfit().create(PsnCoinRetrofitInterface::class.java)

        psnCoinService.getPsnSearchCoinAll().enqueue(object : Callback<PsnCoinSearchResponse>{
            override fun onResponse(
                call: Call<PsnCoinSearchResponse>,
                response: Response<PsnCoinSearchResponse>
            ) {
                psnCoinSearchView.getCoinsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PsnCoinSearchResponse>, t: Throwable) {
                psnCoinSearchView.getCoinsAllFailure("t")
            }
        })
    }

    // 소유코인 창에서 버튼을 눌렀을 때 psnCoinAddInfo 객체를 만들어서 아래 함수의 매개변수로 넣어줌
    fun getPsnCoinAdd(psnCoinAddInfo: PsnCoinAddInfo){
        val psnCoinService= getRetorfit().create(PsnCoinRetrofitInterface::class.java)

        psnCoinService.getPsnCoinAdd(psnCoinAddInfo).enqueue(object : Callback<PsnCoinAddResponse> {
            override fun onResponse( // 통신 성공
                call: Call<PsnCoinAddResponse>,
                response: Response<PsnCoinAddResponse>
            ) {
                Log.d("psnCoinAddSuccess", "소유 코인 추가 성공")
                psnCoinAddView.psnCoinAddSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PsnCoinAddResponse>, t: Throwable) { // 통신 실패
                Log.d("psnCoinAddFailure", "소유 코인 추가 실패")
                psnCoinAddView.psnCoinAddFailure("t")
            }
        })
    }

    fun getPsnCoinAddTrade(psnCoinAddTradeInfo: PsnCoinAddTradeInfo){
        val psnCoinService = getRetorfit().create(PsnCoinRetrofitInterface::class.java)

        psnCoinService.getPsnCoinAddTrade(psnCoinAddTradeInfo).enqueue(object : Callback<PsnCoinAddTradeResponse>{
            override fun onResponse(
                call: Call<PsnCoinAddTradeResponse>,
                response: Response<PsnCoinAddTradeResponse>
            ) {
                Log.d("psnCoinAddTradeSuccess", "거래 내역 생성 성공")
                psnCoinAddTradeView.psnCoinAddTradeSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PsnCoinAddTradeResponse>, t: Throwable) {
                Log.d("psnCoinAddTradeFailure", "거래 내역 생성 실패")
                psnCoinAddTradeView.psnCoinAddTradeFailure("t")
            }
        })
    }
}