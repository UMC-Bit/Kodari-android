package com.bit.kodari.Util.Upbit

import android.util.Log
import com.bit.kodari.Main.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UpbitService {
    // BASE URL
    val BASE_URL_UPBIT_API = "https://api.upbit.com/"

    /*
        업비트 현재 코인 시세
        코인 List를 받아서 현재 코인 가격 List를 반환하는 함수
     */
    fun getCurrentPrice(coinList: List<String>): List<Int> {
        val currentPriceList = ArrayList<Int>()
        // Retrofit 초기 설정
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_UPBIT_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(UpbitInterface::class.java)
        // 코인 List 크기만큼 반복문 실행, 현재 가격을 List에 추가
        for(i in 0 until coinList.size){
            val callGetUpbitPrice = api.getCurrentPrice("application/json", "KRW-"+coinList[i])
            callGetUpbitPrice.enqueue(object : Callback<List<UpbitPrice>> {
                override fun onResponse(
                    call: Call<List<UpbitPrice>>,
                    response: Response<List<UpbitPrice>>
                ) {
                    // 코인의 현재 가격을 받아옴
                    val price: Int? = response.body()?.get(0)?.trade_price
                    if (price != null) {
                        currentPriceList.add(price)
                    }else{
                        currentPriceList.add(0)
                    }
                    val homeFragment = HomeFragment()
                    homeFragment.setRepresentPV()
                    homeFragment.setRepresentRV()
                    Log.d("결과", "성공: ${response.body()}")

                }
                override fun onFailure(call: Call<List<UpbitPrice>>, t: Throwable) {
                    Log.d("결과", "실패")
                }
            })
        }
        return currentPriceList
    }
}

