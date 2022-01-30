package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebatePostResponse

interface DebateMainView {
    fun getPostsAllSuccess(resp : DebatePostResponse)
    fun getPostsAllFailure(message:String)
}