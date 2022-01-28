package com.bit.kodari.Login.Retrofit

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInService(val context: Context) {

    //회원가입 API 호출
    fun getSignUp(signUpInfo: SignUpInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)

        logInService.getSignUp(signUpInfo).enqueue(object : Callback<SignUpResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                val resp = response.body()!!
                when(resp.code){
                  1000 -> {
                      Toast.makeText(context,"회원가입 성공" , Toast.LENGTH_SHORT).show()
                  }
                  else -> {
                      Toast.makeText(context,"회원가입 실패 , ${resp.message}" , Toast.LENGTH_LONG).show()
                  }
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {      //통신 실패 했을때
                Log.d("signUp" ,"통신 실패 : ${t}")
            }
        })
    }


    //로그인 API
    fun getLogIn(logInInfo: LogInInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getLogIn(logInInfo).enqueue(object : Callback<LogInResponse>{
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
                val resp = response.body()!!
                when(resp.code){
                  1000 -> {
                      Toast.makeText(context, "로그인 성공" , Toast.LENGTH_LONG).show()
                  }
                  else -> {
                      Toast.makeText(context, "로그인 실패 ${resp.message}" , Toast.LENGTH_LONG).show()
                  }
                }
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.d("LogIn" ,"통신 실패 : ${t}")
            }
        })
    }


}