package com.bit.kodari.Login.RetrofitData

data class LogInResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: LogInResult
)