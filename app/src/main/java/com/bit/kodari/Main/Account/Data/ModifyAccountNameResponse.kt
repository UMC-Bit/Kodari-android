package com.bit.kodari.Main.Account.Data

data class ModifyAccountNameResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)