package com.bit.kodari.Debate.Data

import com.google.gson.annotations.SerializedName

data class DebateSelectPostComment(
    @SerializedName("checkCommentWriter") val checkCommentWriter: Boolean,
    @SerializedName("comment_status") val comment_status: String,
    @SerializedName("content") val content: String,
    @SerializedName("like") val like: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("postCommentIdx") val postCommentIdx: Int,
    @SerializedName("profileImgUrl") val profileImgUrl: Any,
    @SerializedName("replyList") val replyList: ArrayList<DebateSelectPostReply>,
    @SerializedName("time") val time: String,
    @SerializedName("userIdx") val userIdx: Int
)