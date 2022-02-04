package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.PossessionCoin.RetrofitData.*
import retrofit2.Call
import retrofit2.http.*

interface PsnCoinRetrofitInterface {

    @GET("/coins")
    fun getPsnSearchCoinAll():Call<PsnCoinSearchResponse> // 응답받는 response 형식으로 맞춰주기

    // 소유 코인 추가 API(get방식은 받는거라서 따로 만들어서 보내줄 필요가 없지만 post형식은 body를 보내줘야 response가 온다.)
    @POST("/userCoin/post")
    fun getPsnCoinAdd(@Body psnCoinAddInfo: PsnCoinAddInfo):Call<PsnCoinAddResponse> // psncoinaddinfo 객체를 보냄

    // 거래 내역 생성 API
    @POST("/trades/post")
    fun getPsnCoinAddTrade(@Body psnCoinAddTradeInfo: PsnCoinAddTradeInfo):Call<PsnCoinAddTradeResponse>

    // 소유 코인 조회
    @GET("/userCoin/{userIdx}")
    fun getPsnCoinInquire(@Header("X-ACCESS-TOKEN") jwt :String , @Path("userIdx") userIdx : Int):Call<PsnCoinMgtInsquireResponse>

    //fun getPsnCoinInquire(@Query("coinName") coinName : String):Call<PsnCoinMgtInsquireResponse> ? 일 때, 위에는 : 일 때
}