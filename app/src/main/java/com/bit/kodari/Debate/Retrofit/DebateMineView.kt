package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.CommentData.*
import com.bit.kodari.Debate.LikeData.CommentLikeResponse
import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.DebateSelectPostResponse
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse

interface DebateMineView {

    fun selectPostSuccess(response : DebateSelectPostResponse)
    fun selectPostFailure(message : String)
    fun postLikeSuccess(response: PostLikeResponse , like:Int)
    fun postLikeFailure(message: String)
    fun regCommentSuccess(response: RegCommentResponse)
    fun regCommentFailure(message: String)
    fun modifyCommentSuccess(response:ModifyCommentResponse)
    fun modifyCommentFailure(message:String)
    fun deleteCommentSuccess(response:DeleteCommentResponse)
    fun deleteCommentFailure(message: String)
    fun regReCommentSuccess(response:RegReCommentResponse)
    fun regReCommentFailure(message:String)
    fun deleteReCommentSuccess(response:DeleteReCommentResponse)
    fun deleteReCommentFailure(message: String)
    fun pressCommentLikeSuccess(response: CommentLikeResponse)
    fun pressCommentLikeFailure(message: String)
}