package com.bit.kodari.Util.BItfinex

import android.util.Log
import com.bit.kodari.Util.Upbit.UpbitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BinanceService {
    // BASE URl
    val BASE_URL_BINANCE_API = "https://api.binance.com"

    /*
        바이낸스 현재 코인 시세
       코인 리스트를 받아서 현재 코인 가격의 리스트를 반환하는 함수
     */
    fun getCurrentPrice(coinList: List<String>): List<Int>{
        val currentPriceList = mutableListOf<Int>()
        // Retrofit 초기 설정
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_BINANCE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(BinanceInterface::class.java)
        // 코인 리스트 크기만큼 반복문 실행, 현재 가격을 리스트에 추가
        for(i in 1 until coinList.size) {
            val callGetBinancePrice = api.getCurrentPrice(coinList[i] + "USDT")
            callGetBinancePrice.enqueue(object : Callback<List<BinanceResult>> {
                override fun onResponse(
                    call: Call<List<BinanceResult>>,
                    response: Response<List<BinanceResult>>
                ) {
                    // 코인의 현재 가격을 받아옴
                    val price: Int = response.body()!!.get(0)!!.price
                    if (price != null) {
                        currentPriceList.add(i, price)
                    } else {
                        currentPriceList.add(i, 0)
                    }
                    Log.d("결과", "성공: ${response.body()}")
                }

                override fun onFailure(call: Call<List<BinanceResult>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        return currentPriceList
    }
}