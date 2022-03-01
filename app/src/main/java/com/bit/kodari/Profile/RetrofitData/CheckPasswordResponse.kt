package com.bit.kodari.Profile.RetrofitData

data class CheckPasswordResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)