package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.Data.DebateUpdatePostResponse

interface DebatePostWriteVIew {
    fun updatePostSuccess(response:DebateUpdatePostResponse)
    fun updatePostFailure(message:String)
}