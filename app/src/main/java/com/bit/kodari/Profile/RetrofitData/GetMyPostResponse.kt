package com.bit.kodari.Profile.RetrofitData

import com.google.gson.annotations.SerializedName

data class GetMyPostResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<GetMyPostResult>
)