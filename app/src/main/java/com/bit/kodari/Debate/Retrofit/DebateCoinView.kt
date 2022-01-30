package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateCoinResponse

interface DebateCoinView {

    fun getCoinsAllSuccess(response:DebateCoinResponse)
    fun getCoinsAllFailure(message:String)

}