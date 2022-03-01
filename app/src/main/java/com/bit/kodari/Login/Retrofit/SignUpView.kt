package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.SignUpResponse

interface SignUpView {
    fun signUpSuccess(response: SignUpResponse)         //회원 가입 API 통신 성공했을 때
    fun signUpFailure(message:String)         //회원 가입 API 통신 실패했을 때
}