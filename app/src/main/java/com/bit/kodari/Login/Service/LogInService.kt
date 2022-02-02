package com.bit.kodari.Login.Service

import android.util.Log
import com.bit.kodari.Login.Retrofit.LoginRetrofitInterface
import com.bit.kodari.Login.Retrofit.LogInView
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInService() {

    private lateinit var signUpView: SignUpView
    private lateinit var logInView: LogInView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLogInView(logInView : LogInView){
        this.logInView = logInView
    }

    //회원가입 API 호출
    fun getSignUp(signUpInfo: SignUpInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)

        logInService.getSignUp(signUpInfo).enqueue(object : Callback<SignUpResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                Log.d("signUp" , "통신 성공")
                signUpView.signUpSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {      //통신 실패 했을때
                Log.d("signUp" , "통신 실패")
                signUpView.signUpFailure("${t}")
            }
        })
    }


    //로그인 API
    fun getLogIn(logInInfo: LogInInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getLogIn(logInInfo).enqueue(object : Callback<LogInResponse>{
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
                logInView.logInSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.d("LogIn" ,"통신 실패 : ${t}")
                logInView.logInFailure("${t}")
            }
        })
    }
}