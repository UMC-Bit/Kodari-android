package com.bit.kodari.Util.Coin


import com.bit.kodari.Util.Coin.Binance.BinanceResult
import com.bit.kodari.Util.Coin.Bithumb.BithumbResponse
import com.bit.kodari.Util.Coin.Upbit.UpbitResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinInterface {
    // 업비트
    @GET("v1/ticker")
    fun getCurrentUpbitPrice(
        @Header("Accept") accept: String,
        @Query("markets") markets: String
    ): Call<List<UpbitResult>>

    // 바이낸스
    @GET("/api/v3/ticker/price")
    fun getCurrentBinancePrice(
    ): Call<BinanceResult>

    // 빗썸
    // https://api.bithumb.com/public/ticker/{order_currency}_{payment_currency}
    @GET("/public/ticker/{symbol}")
    fun getCurrentBithumbPrice(
        @Path("symbol") symbol: String): Call<BithumbResponse>
}