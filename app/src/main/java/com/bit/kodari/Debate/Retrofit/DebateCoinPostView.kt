package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateCoinPostResponse

interface DebateCoinPostView {
    fun getCoinPostSuccess(response: DebateCoinPostResponse)
    fun getCoinPostFailure(message:String)
}