package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class CommentLikeResult(
    @SerializedName("commentLikeIdx") val commentLikeIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)