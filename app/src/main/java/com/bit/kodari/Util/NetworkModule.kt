package com.bit.kodari.Util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetorfit() : Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://kodari.shop/")          //retrofit 객체 생성
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}