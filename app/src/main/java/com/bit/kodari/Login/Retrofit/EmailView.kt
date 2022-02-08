package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.EmailResponse

interface EmailView {
    fun getEmailSuccess(response: EmailResponse)
    fun getEmailFailure(message:String)
}