package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Main.Data.MakePortRequest
import com.bit.kodari.Main.Data.MakePortResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HomeRetrofitInterface {
    @POST("/portfolio/post")
    fun makePort(@Header("X-ACCESS-TOKEN") jwt: String, @Body makePortRequest: MakePortRequest) : Call<MakePortResponse>


}