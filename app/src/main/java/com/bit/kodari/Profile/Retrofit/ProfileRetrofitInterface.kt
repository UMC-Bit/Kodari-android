package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Profile.RetrofitData.UpdateProfileImgResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ProfileRetrofitInterface {

    @GET("/app/users/get")
    fun getProfile(@Query("userIdx") userIdx:Int) :Call<GetProfileResponse>


    @PATCH("/app/users/update/nickName/{userIdx}")
    fun updateName(@Header("X-ACCESS-TOKEN") jwt:String, @Path("userIdx") userIdx:Int,
                   @Query("nickName") nickName:String) : Call<UpdateNameResponse>

    @PATCH("app/users/update/profileImgUrl/{userIdx}")
    fun updateProfileImg(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int,
    @Query("profileImgUrl") profileImgUrl:String) : Call<UpdateProfileImgResponse>
}