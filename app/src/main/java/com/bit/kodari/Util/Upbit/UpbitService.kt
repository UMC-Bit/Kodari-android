package com.bit.kodari.Util.Upbit

import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UpbitService {
    val BASE_URL_UPBIT_API = "https://api.upbit.com/"

    fun getCurrentPrice() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_UPBIT_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(UpbitAPI::class.java)
        val callGetUpbitPrice = api.getCurrentPrice("application/json", "KRW-BTC")

        callGetUpbitPrice.enqueue(object : Callback<List<UpbitPrice>> {
            override fun onResponse(
                call: Call<List<UpbitPrice>>,
                response: Response<List<UpbitPrice>>
            ) {
                Log.d("결과", "성공: ${response.body()}")
            }

            override fun onFailure(call: Call<List<UpbitPrice>>, t: Throwable) {
                Log.d("결과", "실패")
            }
        })
    }
}

