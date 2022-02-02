package com.bit.kodari.Profile.RetrofitData

import com.google.gson.annotations.SerializedName

data class GetMyCommentResult(
    @SerializedName("content") val content: String,
    @SerializedName("postList") val postList: ArrayList<GetMyCommentPostResponse>,
    @SerializedName("time") val time: String
)