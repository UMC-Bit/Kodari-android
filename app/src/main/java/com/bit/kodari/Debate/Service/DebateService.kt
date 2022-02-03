package com.bit.kodari.Debate.Service

import android.util.Log
import com.bit.kodari.Debate.Data.*
import com.bit.kodari.Debate.Retrofit.*
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import com.bit.kodari.Util.getUserIdx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateService {

    private lateinit var debateCoinView: DebateCoinView
    private lateinit var debateMainView: DebateMainView
    private lateinit var debateCoinPostView: DebateCoinPostView
    private lateinit var debatePostWriteVIew: DebatePostWriteVIew
    private lateinit var debateMineView: DebateMineView

    fun setDebateCoinView(debateCoinView:DebateCoinView){
        this.debateCoinView = debateCoinView
    }

    fun setDebateMainView(debateMainView: DebateMainView){
        this.debateMainView = debateMainView
    }

    fun setDebateCoinPostView(debateCoinPostView: DebateCoinPostView){
        this.debateCoinPostView = debateCoinPostView
    }

    fun setDebatePostWriteVIew(debatePostWriteVIew: DebatePostWriteVIew){
        this.debatePostWriteVIew = debatePostWriteVIew
    }

    fun setDebateMineView(debateMineView: DebateMineView){
        this.debateMineView = debateMineView
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
                debateCoinPostView.getCoinPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateCoinPostResponse>, t: Throwable) {
                debateCoinPostView.getCoinPostFailure("${t}")
            }
        })
    }

    fun writePost(post:DebateWritePostRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.writePost(getJwt()!!, post).enqueue(object : Callback<DebateWritePostResponse>{
            override fun onResponse(
                call: Call<DebateWritePostResponse>,
                response: Response<DebateWritePostResponse>
            ) {
                Log.d("post" , "${response.code()} ")
                debatePostWriteVIew.updatePostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateWritePostResponse>, t: Throwable) {
                debatePostWriteVIew.updatePostFailure("${t}")
            }
        })
    }

    fun selectPost(postIdx: Int){       //
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.selectPost(getJwt()!!,postIdx).enqueue(object : Callback<DebateSelectPostResponse>{
            override fun onResponse(
                call: Call<DebateSelectPostResponse>,
                response: Response<DebateSelectPostResponse>
            ) {
                Log.d("selectPost" , "코드 : ${response.body()!!.code}")
                Log.d("selectPost" , "코드 : ${response.body()!!}")
                debateMineView.selectPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateSelectPostResponse>, t: Throwable) {
                debateMineView.selectPostFailure("${t}")
            }
        })
    }

    fun getUserInfo(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getUserInfo(getUserIdx()).enqueue(object : Callback<GetProfileResponse>{
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                debatePostWriteVIew.getUserInfoSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                debatePostWriteVIew.getUserInfoFailure("${t}")
            }
        })
    }
}