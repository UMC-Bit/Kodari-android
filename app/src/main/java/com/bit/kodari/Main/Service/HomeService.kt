package com.bit.kodari.Main.Service

import android.util.Log
import com.MyApplicationClass
import com.bit.kodari.Main.Data.GetTradeListResponse
import com.bit.kodari.Main.RetrofitInterface.HomeRetrofitInterface
import com.bit.kodari.Main.RetrofitInterface.MemoView
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService {
    private lateinit var memoView: MemoView

    fun setMemoView(memoView: MemoView){
        this.memoView = memoView
    }

    fun getTradeList(coinIdx:Int){
        val homeService = getRetorfit().create(HomeRetrofitInterface::class.java)
        homeService.getTradeList(getJwt()!!, MyApplicationClass.myPortIdx , coinIdx).enqueue(object :
            Callback<GetTradeListResponse>{
            override fun onResponse(
                call: Call<GetTradeListResponse>,
                response: Response<GetTradeListResponse>
            ) {
                when(response.body()!!.code){
                  1000 -> {memoView.getTradeListSuccess(response.body()!!)}
                  else -> {memoView.getTradeListFailure(response.body()!!.message)}
                }
            }

            override fun onFailure(call: Call<GetTradeListResponse>, t: Throwable) {
                Log.d("getMemoList" , t.toString())
            }
        })
    }
}