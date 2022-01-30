package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ProfileRetrofitInterface {

    @PUT("/app/users/update/nickName/{userIdx}")
    fun updateName(@Header("X-ACCESS-TOKEN") jwt:String, @Path("userIdx") userIdx:Int,
                   @Query("nickName") nickName:String) : Call<UpdateNameResponse>
}