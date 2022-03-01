package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.LogInResponse

interface LogInView {
    fun logInSuccess(response:LogInResponse)
    fun logInFailure(message:String)
}