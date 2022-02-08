package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.PasswordResponse

interface PasswordView {
    fun getPasswordSuccess(response: PasswordResponse)
    fun getPasswordFailure(message:String)
}