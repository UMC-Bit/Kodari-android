package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Main.Data.GetProfitResponse

interface HomeView {
    fun getDayProfitSuccess(response:GetProfitResponse)
    fun getDayProfitFrailure(message:String)
    fun getWeekProfitSuccess(response: GetProfitResponse)
    fun getWeekProfitFailure(message: String)
    fun getMonthProfitSuccess(response: GetProfitResponse)
    fun getMonthProfitFailure(message: String)
}