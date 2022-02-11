package com.bit.kodari.Login.RetrofitData

data class NicknameResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)