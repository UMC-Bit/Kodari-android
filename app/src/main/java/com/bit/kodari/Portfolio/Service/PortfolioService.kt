package com.bit.kodari.Portfolio.Service

import android.util.Log
import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Portfolio.Adapter.SearchCoinView
import com.bit.kodari.Portfolio.Data.SearchCoinResponse
import com.bit.kodari.Portfolio.Retrofit.PortfolioInterface
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortfolioService {

    private lateinit var portfolioView: PortfolioView
    private lateinit var searchCoinView: SearchCoinView

    fun setPortfolioView(portfolioView: PortfolioView){
        this.portfolioView = portfolioView
    }

    fun setSearchCoinView(searchCoinView: SearchCoinView){
        this.searchCoinView = searchCoinView
    }

    // 유저 인덱스로 포트폴리오 List 조회
    fun getPortfolioList(userIdx: Int){
        val portService = getRetorfit().create(PortfolioInterface::class.java)
        portService.getAllPortfolio(userIdx, getJwt()).enqueue(object: Callback<PortIdxResponse> {
            override fun onResponse(
                call: Call<PortIdxResponse>,
                response: Response<PortIdxResponse>
            ) {
                Log.d("portIdx", "포트폴리오 리스트 불러오기 성공!")
                if(response.body()!!.result.size > 0){
                    val portIdx = response.body()!!.result!![0].portIdx
                    getPortfolioInfo(portIdx)
                }
            }
            override fun onFailure(call: Call<PortIdxResponse>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("portIdx", "불러오기 실패")
            }
        })
    }

    // 포트폴리오 인덱스로 포트폴리오 내용 조회
    fun getPortfolioInfo(portIdx: Int){
        val portService = getRetorfit().create(PortfolioInterface::class.java)
        portService.getPortfolioByPortIdx(portIdx, getJwt()).enqueue(object: Callback<PortfolioResponse>{
            override fun onResponse(
                call: Call<PortfolioResponse>,
                response: Response<PortfolioResponse>
            ) {
                Log.d("portFolio", response.body().toString())
                portfolioView.portfolioSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PortfolioResponse>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("portIdx", "불러오기 실패")
            }

        })
    }

    // 코인 목록 불러오기
    fun getCoinsAll(){
        val portfolioService = getRetorfit().create(PortfolioInterface::class.java)

        portfolioService.getSearchCoinAll().enqueue(object : Callback<SearchCoinResponse> {
            override fun onResponse(
                call: Call<SearchCoinResponse>,
                response: Response<SearchCoinResponse>
            ) {
                searchCoinView.getSearchCoinAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SearchCoinResponse>, t: Throwable) {
                searchCoinView.getSearchCoinAllFailure("t")
            }
        })
    }
}
