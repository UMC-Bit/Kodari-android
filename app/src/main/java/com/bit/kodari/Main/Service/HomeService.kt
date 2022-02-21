package com.bit.kodari.Main.Service

import android.util.Log
import com.MyApplicationClass
import com.bit.kodari.Main.Data.GetProfitResponse
import com.bit.kodari.Main.Data.GetTradeListResponse
import com.bit.kodari.Main.RetrofitInterface.HomeRetrofitInterface
import com.bit.kodari.Main.RetrofitInterface.HomeView
import com.bit.kodari.Main.RetrofitInterface.MainView
import com.bit.kodari.Main.RetrofitInterface.MemoView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService {
    private lateinit var memoView: MemoView
    private lateinit var homeView: HomeView
    private lateinit var mainView: MainView

    fun setMemoView(memoView: MemoView) {
        this.memoView = memoView
    }

    fun setHomeView(homeView: HomeView) {
        this.homeView = homeView
    }

    fun setMainView(mainView: MainView){
        this.mainView = mainView
    }

    //메모에 거래 내역 가져오기
    fun getTradeList(coinIdx: Int) {
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getTradeList(getJwt()!!, MyApplicationClass.myPortIdx, coinIdx).enqueue(object :
            Callback<GetTradeListResponse> {
            override fun onResponse(
                call: Call<GetTradeListResponse>,
                response: Response<GetTradeListResponse>
            ) {
                when (response.body()!!.code) {
                    1000 -> {
                        memoView.getTradeListSuccess(response.body()!!)
                    }
                    else -> {
                        memoView.getTradeListFailure(response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetTradeListResponse>, t: Throwable) {
                Log.d("getMemoList", t.toString())
            }
        })
    }

    fun getDayProfit(accountIdx: Int) {
        Log.d("getDayProfit", "${accountIdx}")
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getDayProfit(getJwt()!!, accountIdx)
            .enqueue(object : Callback<GetProfitResponse> {
                override fun onResponse(
                    call: Call<GetProfitResponse>,
                    response: Response<GetProfitResponse>
                ) {
                    when (response.body()!!.code) {
                        1000 -> {
                            Log.d("프로핏","${response.body()!!.result.size}")
                            homeView.getDayProfitSuccess(response.body()!!)
                        }
                        else -> {
                            homeView.getDayProfitFrailure(response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<GetProfitResponse>, t: Throwable) {
                    Log.d("getDayProfit", t.toString())
                }
            })
    }

    fun getWeekProfit(accountIdx: Int) {
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getWeekProfit(getJwt()!!, accountIdx)
            .enqueue(object : Callback<GetProfitResponse> {
                override fun onResponse(
                    call: Call<GetProfitResponse>,
                    response: Response<GetProfitResponse>
                ) {
                    when (response.body()!!.code) {
                        1000 -> {
                            Log.d("프로핏","${response.body()!!.result.size}")
                            homeView.getWeekProfitSuccess(response.body()!!)
                        }
                        else -> {
                            homeView.getWeekProfitFailure(response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<GetProfitResponse>, t: Throwable) {
                    Log.d("getDayProfit", t.toString())
                }
            })
    }

    fun getMonthProfit(accountIdx: Int) {
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getMonthProfit(getJwt()!!, accountIdx)
            .enqueue(object : Callback<GetProfitResponse> {
                override fun onResponse(
                    call: Call<GetProfitResponse>,
                    response: Response<GetProfitResponse>
                ) {
                    when (response.body()!!.code) {
                        1000 -> {
                            Log.d("프로핏","${response.body()!!.result.size}")
                            homeView.getMonthProfitSuccess(response.body()!!)
                        }
                        else -> {
                            homeView.getMonthProfitFailure(response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<GetProfitResponse>, t: Throwable) {
                    Log.d("getDayProfit", t.toString())
                }
            })
    }

    fun getUserFromEmail(email:String){
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getUserFromEmail(email).enqueue(object : Callback<GetProfileResponse>{
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                when(response.body()!!.code){
                  1000 -> {
                      mainView.getUserSuccess(response.body()!!)
                  }
                  else -> {
                      mainView.getUserFailure(response.body()!!.message)
                  }
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                mainView.getUserFailure("$t")
            }
        })
    }
}