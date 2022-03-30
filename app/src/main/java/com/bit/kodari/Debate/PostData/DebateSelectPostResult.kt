package com.bit.kodari.Debate.PostData

import com.google.gson.annotations.SerializedName

data class DebateSelectPostResult(
    @SerializedName("checkWriter") val checkWriter: Boolean,
    @SerializedName("commentList") val commentList: ArrayList<DebateSelectPostComment>,
    @SerializedName("comment_cnt") val comment_cnt: Int,
    @SerializedName("content") val content: String,
    @SerializedName("dislike") val dislike: Int,
    @SerializedName("like") val like: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("postIdx") val postIdx: Int,
    @SerializedName("profileImgUrl") val profileImgUrl: Any,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("time") val time: String,
    @SerializedName("checkPostLike") var checkPostLike :Boolean,
    @SerializedName("checkPostDislike") var checkPostDislike:Boolean
)