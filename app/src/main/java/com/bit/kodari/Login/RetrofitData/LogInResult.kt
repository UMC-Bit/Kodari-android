package com.bit.kodari.Login.RetrofitData

data class LogInResult(
    val jwt: String,
    val userIdx: Int
)