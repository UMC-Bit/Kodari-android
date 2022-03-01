package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.PostData.DebateDeletePostResponse

interface DebateDeletePostView {
    fun deletePostSuccess(response:DebateDeletePostResponse)
    fun deletePostFailure(message:String)
}