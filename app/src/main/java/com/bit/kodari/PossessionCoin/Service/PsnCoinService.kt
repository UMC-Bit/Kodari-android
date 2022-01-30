package com.bit.kodari.PossessionCoin.Service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bit.kodari.Debate.Data.DebateCoinResponse
import com.bit.kodari.Debate.Retrofit.DebateCoinView
import com.bit.kodari.Debate.Retrofit.DebateRetrofitInterface
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinRetrofitInterface
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinSearchView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 여기서 함수들을 호출
class PsnCoinService{ // PsnCoinService의 매개변수는 없어도 되나 toast 메시지를 위한 매개변수

    private lateinit var psnCoinSearchView: PsnCoinSearchView

    fun setPsnCoinSearchView(psnCoinSearchView: PsnCoinSearchView){
        this.psnCoinSearchView = psnCoinSearchView
    }

    // 소유코인 창에서 버튼을 눌렀을 때 psnCoinAddInfo 객체를 만들어서 아래 함수의 매개변수로 넣어줌
    fun getPsnCoinAdd(psnCoinAddInfo: PsnCoinAddInfo){
        val psnCoinService= getRetorfit().create(PsnCoinRetrofitInterface::class.java)

        psnCoinService.getPsnCoinAdd(psnCoinAddInfo).enqueue(object : Callback<PsnCoinAddResponse> {
            override fun onResponse( // 통신 성공
                call: Call<PsnCoinAddResponse>,
                response: Response<PsnCoinAddResponse>
            ) {
                val resp = response.body()!! // 성공하면 결과가 있으므로 !!
                when(resp.code){
//                    1000 ->{
//                        Toast.makeText(context,"소유 코인 등록 성공", Toast.LENGTH_SHORT).show()
//                    }
//                    else->{
//                        Toast.makeText(context,"소유 코인 등록 실패, ${resp.message}", Toast.LENGTH_SHORT).show()
//                    }
                }
            }

            override fun onFailure(call: Call<PsnCoinAddResponse>, t: Throwable) { // 통신 실패
                Log.d("PsnCoinAdd", "통신 실패: ${t}")
            }

        })
    }


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
}