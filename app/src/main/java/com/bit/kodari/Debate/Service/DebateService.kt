package com.bit.kodari.Debate.Service

import android.util.Log
import com.bit.kodari.Debate.Data.DebateCoinResponse
import com.bit.kodari.Debate.Data.DebatePostResponse
import com.bit.kodari.Debate.Retrofit.DebateCoinView
import com.bit.kodari.Debate.Retrofit.DebateMainView
import com.bit.kodari.Debate.Retrofit.DebateRetrofitInterface
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateService {

    private lateinit var debateCoinView: DebateCoinView
    private lateinit var debateMainView: DebateMainView

    fun setDebateCoinView(debateCoinView:DebateCoinView){
        this.debateCoinView = debateCoinView
    }

    fun setDebateMainView(debateMainView: DebateMainView){
        this.debateMainView = debateMainView
    }


    fun getCoinsAll(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)

        debateService.getCoinsAll().enqueue(object : Callback<DebateCoinResponse>{
            override fun onResponse(
                call: Call<DebateCoinResponse>,
                response: Response<DebateCoinResponse>
            ) {
                debateCoinView.getCoinsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateCoinResponse>, t: Throwable) {
                debateCoinView.getCoinsAllFailure("t")
            }
        })
    }

    fun getPostsAll(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getPostsAll().enqueue(object : Callback<DebatePostResponse>{
            override fun onResponse(
                call: Call<DebatePostResponse>,
                response: Response<DebatePostResponse>
            ) {
                Log.d("debate" , "호출 성공")
                debateMainView.getPostsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebatePostResponse>, t: Throwable) {
                Log.d("debate" , "호출 실패")
                debateMainView.getPostsAllFailure("${t}")
            }
        })
    }
}