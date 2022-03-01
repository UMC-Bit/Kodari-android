package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class CommentLikeRequest(
    @SerializedName("postCommentIdx") val postCommentIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)