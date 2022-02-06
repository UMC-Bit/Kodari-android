package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.DebateCoinPostResponse

interface DebateCoinPostView {
    fun getCoinPostSuccess(response: DebateCoinPostResponse)
    fun getCoinPostFailure(message:String)
//    fun postLikeSuccess(resp: PostLikeResponse)         //게시글 좋아요 눌렀을때
//    fun postLikeFailure(message:String)
}