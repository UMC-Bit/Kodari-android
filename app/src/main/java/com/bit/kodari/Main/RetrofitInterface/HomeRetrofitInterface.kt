package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Main.Data.GetTradeListResponse
import com.bit.kodari.Main.Data.MakePortRequest
import com.bit.kodari.Main.Data.MakePortResponse
import retrofit2.Call
import retrofit2.http.*

interface HomeRetrofitInterface {
    //거래내역 조회
    @GET("/trades/get/{portIdx}/{coinIdx}")
    fun getTradeList(@Header("X-ACCESS-TOKEN") jwt:String , @Path("portIdx") portIdx:Int , @Path("coinIdx") coinIdx:Int) : Call<GetTradeListResponse>

}