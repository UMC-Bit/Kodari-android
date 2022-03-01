package com.bit.kodari.Profile.RetrofitData

import com.google.gson.annotations.SerializedName

data class GetProfileResult(
    @SerializedName("email") val email: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("password") val password: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String,
    @SerializedName("status") val status: String,
    @SerializedName("userIdx") val userIdx: Int
)