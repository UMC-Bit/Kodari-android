package com.bit.kodari.Profile.RetrofitData

data class UpdateNameResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)