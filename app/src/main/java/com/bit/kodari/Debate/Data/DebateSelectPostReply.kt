package com.bit.kodari.Debate.Data

import com.google.gson.annotations.SerializedName

data class DebateSelectPostReply(
    @SerializedName("checkReplyWriter") val checkReplyWriter: Boolean,
    @SerializedName("content") val content: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("postReplyIdx") val postReplyIdx: Int,
    @SerializedName("profileImgUrl") val profileImgUrl: Any,
    @SerializedName("reply_status") val reply_status: String,
    @SerializedName("time") val time: String,
    @SerializedName("userIdx") val userIdx: Int
)