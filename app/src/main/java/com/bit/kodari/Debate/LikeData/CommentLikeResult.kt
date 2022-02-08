package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class CommentLikeResult(
    @SerializedName("commentLikeidx") val commentLikeIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)