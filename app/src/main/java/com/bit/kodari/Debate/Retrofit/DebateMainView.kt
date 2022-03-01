package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.DebatePostResponse

interface DebateMainView {
    fun getPostsAllSuccess(resp : DebatePostResponse)
    fun getPostsAllFailure(message:String)
}