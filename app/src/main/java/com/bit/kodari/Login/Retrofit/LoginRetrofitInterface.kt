package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("/app/users/sign-up")
    fun getSignUp(@Body signUpInfo: SignUpInfo) : Call<SignUpResponse>

    @POST("/app/users/log-in")
    fun getLogIn(@Body logInInfo: LogInInfo) : Call<LogInResponse>
}