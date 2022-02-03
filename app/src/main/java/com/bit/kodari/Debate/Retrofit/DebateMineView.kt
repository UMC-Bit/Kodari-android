package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateSelectPostResponse

interface DebateMineView {

    fun selectPostSuccess(response : DebateSelectPostResponse)
    fun selectPostFailure(message : String)
}