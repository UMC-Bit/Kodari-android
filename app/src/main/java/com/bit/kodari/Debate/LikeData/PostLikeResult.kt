package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class PostLikeResult(
    @SerializedName("postLikeIdx") val postLikeIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)