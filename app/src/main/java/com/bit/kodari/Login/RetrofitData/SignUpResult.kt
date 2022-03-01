package com.bit.kodari.Login.RetrofitData

data class SignUpResult(
    val jwt: String,
    val nickName: String,
    val userIdx: Int
)