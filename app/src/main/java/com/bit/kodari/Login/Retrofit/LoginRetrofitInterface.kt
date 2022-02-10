package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("/app/users/sign-up")
    fun getSignUp(@Body signUpInfo: SignUpInfo) : Call<SignUpResponse>

    @POST("/app/users/log-in")
    fun getLogIn(@Body logInInfo: LogInInfo) : Call<LogInResponse>

    @POST("/app/users/get/getCheckEmail/")
    fun getCheckEmail (@Body emailInfo: EmailInfo) : Call<EmailResponse>

    @POST("/app/users/get/getCheckPassword")
    fun getCheckPassword(@Body passwordInfo: PasswordInfo) : Call<PasswordResponse>

    @POST("/app/users/get/getCheckNickName")
    fun getCheckNickname(@Body nicknameInfo: NicknameInfo) : Call<NicknameResponse>


}