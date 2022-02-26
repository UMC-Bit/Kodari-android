package com.bit.kodari.Util.Coin.USD

import android.util.Log
import com.bit.kodari.Login.Retrofit.LoginRetrofitInterface
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Util.Coin.CoinView
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsdService {
    private lateinit var usdView: UsdView
    fun setUsdView(usdView: UsdView) {
        this.usdView = usdView
    }
    //회원가입 API 호출
    fun getUsdExchangeRate(){
        val usdService = getRetorfit().create(UsdInterface::class.java)
        usdService.getUsdExchangeRate().enqueue(object : Callback<UsdResponse> {
            override fun onResponse(call: Call<UsdResponse>, response: Response<UsdResponse>) {
                usdView.usdExchangeSuccess(response.body()!!.result[0].exchagePrice)
            }
            override fun onFailure(call: Call<UsdResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}