package com.bit.kodari.Profile.RetrofitData

import com.google.gson.annotations.SerializedName

data class GetMyCommentPostResponse(
    @SerializedName("comment_cnt") val comment_cnt: Int,
    @SerializedName("content") val content: String,
    @SerializedName("dislike") val dislike: Int,
    @SerializedName("like") val like: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("postIdx") val postIdx: Int,
    @SerializedName("profileImgUrl") val profileImgUrl: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("time") val time: String
)