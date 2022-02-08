package com.bit.kodari.Util.Binance

import android.util.Log
import com.bit.kodari.Main.HomeFragment
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object BinanceService {
    // BASE URl
    val BASE_URL_BINANCE_API = "https://api.binance.com"

    /*
        바이낸스 현재 코인 시세
       코인 리스트를 받아서 현재 코인 가격의 리스트를 반환하는 함수
     */
    fun getCurrentPrice(coinList: List<String>): HashMap<String, Double>{
        val coinMap = HashMap<String, Double>()
        // Retrofit 초기 설정
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_BINANCE_API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create<BinanceInterface>()
        val callBinancePrice = api.getCurrentPrice();
        callBinancePrice.enqueue(object : Callback<List<BinanceResult>> {
                override fun onResponse(
                    call: Call<List<BinanceResult>>,
                    response: Response<List<BinanceResult>>
                ) {
                    // 코인 리스트를 받아와서 coinList에 있는 Symbol이랑 비교하기 위한 for문
                    for(i in response.body()!!.indices){
                        val coinSymbol = response.body()!![i].symbol+"USDT"
                        if(coinList.contains(coinSymbol)){
                            val price = response.body()!![i].price
                            coinMap.put(coinSymbol, price)
                            Log.d("결과", "성공: ${coinSymbol}: ${price}")
                        }
                    }
                    Log.d("결과", "성공:")
                }
                override fun onFailure(call: Call<List<BinanceResult>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        return coinMap
    }
}
