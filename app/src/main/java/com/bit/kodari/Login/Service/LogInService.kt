package com.bit.kodari.Login.Service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bit.kodari.Debate.Retrofit.DebateCoinView
import com.bit.kodari.Login.Retrofit.LoginRetrofitInterface
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInService(val context: Context) {

    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }
    //회원가입 API 호출
    fun getSignUp(signUpInfo: SignUpInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)

        logInService.getSignUp(signUpInfo).enqueue(object : Callback<SignUpResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                signUpView.signUpSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {      //통신 실패 했을때
                signUpView.signUpFailure("${t}")
            }
        })
    }


    //로그인 API
    fun getLogIn(logInInfo: LogInInfo) {
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getLogIn(logInInfo).enqueue(object : Callback<LogInResponse> {
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {

            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.d("LogIn", "통신 실패 : ${t}")
            }
        })
    }
}