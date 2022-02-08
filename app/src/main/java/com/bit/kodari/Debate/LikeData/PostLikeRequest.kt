package com.bit.kodari.Debate.LikeData

import com.google.gson.annotations.SerializedName

data class PostLikeRequest(
    @SerializedName("likeType") val likeType: Int,
    @SerializedName("postIdx") val postIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)