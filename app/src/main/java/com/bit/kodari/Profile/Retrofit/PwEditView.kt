package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.ChangePasswordResponse
import com.bit.kodari.Profile.RetrofitData.CheckPasswordResponse

interface PwEditView {
    fun checkPasswordSuccess(response: CheckPasswordResponse)
    fun checkPasswordFailure(message:String)
    fun changePasswordSuccess(response: ChangePasswordResponse)
    fun changePasswordFailure(message:String)
}