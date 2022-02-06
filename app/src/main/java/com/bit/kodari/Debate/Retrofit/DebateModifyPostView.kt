package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.PostData.DebateModifyResponse

interface DebateModifyPostView {
    fun updatePostSuccess(response : DebateModifyResponse)         //추후 구현
    fun updatePostFailure(message:String)         //추후 구현

}