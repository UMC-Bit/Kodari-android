package com.bit.kodari.Debate.Service

import android.util.Log
import com.bit.kodari.Debate.Data.*
import com.bit.kodari.Debate.Retrofit.*
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateService {

    private lateinit var debateCoinView: DebateCoinView
    private lateinit var debateMainView: DebateMainView
    private lateinit var debateCoinPostView: DebateCoinPostView
    private lateinit var debatePostWriteVIew: DebatePostWriteVIew

    fun setDebateCoinView(debateCoinView:DebateCoinView){
        this.debateCoinView = debateCoinView
    }

    fun setDebateMainView(debateMainView: DebateMainView){
        this.debateMainView = debateMainView
    }

    fun setDebateCoinPostView(debateCoinPostView: DebateCoinPostView){
        this.debateCoinPostView = debateCoinPostView
    }

    fun setdebatePostWriteVIew(debatePostWriteVIew: DebatePostWriteVIew){
        this.debatePostWriteVIew = debatePostWriteVIew
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

    fun getCoinPost(coinName:String){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getCoinPost(coinName).enqueue(object :Callback<DebateCoinPostResponse>{
            override fun onResponse(
                call: Call<DebateCoinPostResponse>,
                response: Response<DebateCoinPostResponse>
            ) {
                Log.d("Coinpost","${response.isSuccessful} , ${response.message()}")
                debateCoinPostView.getCoinPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateCoinPostResponse>, t: Throwable) {
                debateCoinPostView.getCoinPostFailure("${t}")
            }
        })
    }

    fun updatePost(post:DebateUpdatePostRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.updatePost(post).enqueue(object : Callback<DebateUpdatePostResponse>{
            override fun onResponse(
                call: Call<DebateUpdatePostResponse>,
                response: Response<DebateUpdatePostResponse>
            ) {
                debatePostWriteVIew.updatePostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateUpdatePostResponse>, t: Throwable) {
                debatePostWriteVIew.updatePostFailure("${t}")
            }
        })

    }
}