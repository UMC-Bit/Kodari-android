package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Main.Data.GetProfitResponse
import com.bit.kodari.Main.Data.GetTradeListResponse
import com.bit.kodari.Main.Data.MakePortRequest
import com.bit.kodari.Main.Data.MakePortResponse
import com.bit.kodari.PossessionCoin.RetrofitData.DeleteTradeResponse
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface HomeRetrofitInterface {
    //거래내역 조회
    @GET("/trades/get/{portIdx}/{coinIdx}")
    fun getTradeList(@Header("X-ACCESS-TOKEN") jwt:String , @Path("portIdx") portIdx:Int , @Path("coinIdx") coinIdx:Int) : Call<GetTradeListResponse>
    //수익률 조회
    @GET("/profits/get/daily/{accountIdx}")
    fun getDayProfit(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("accountIdx") accountIdx:Int) : Call<GetProfitResponse>

    @GET("/profits/get/weekly/{accountIdx}")
    fun getWeekProfit(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("accountIdx") accountIdx: Int) : Call<GetProfitResponse>

    @GET("/profits/get/monthly/{accountIdx}")
    fun getMonthProfit(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("accountIdx") accountIdx: Int) : Call<GetProfitResponse>

    //회원 목록 이메일로 조회
    @GET("/app/users/get")
    fun getUserFromEmail(@Query("email") email:String) : Call<GetProfileResponse>

    @PATCH("/trades/delete/{tradeIdx}")
    fun deleteTrade(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("tradeIdx") tradeIdx:Int) : Call<DeleteTradeResponse>

}