package com.bit.kodari.Login.RetrofitData

data class SignUpResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignUpResult
)