package com.bit.kodari.Portfolio.Data

import com.google.gson.annotations.SerializedName

data class PostAccountRequest(
    @SerializedName("accountName") val accountName: String,
    @SerializedName("marketIdx") val marketIdx: String,
    @SerializedName("property") val property: String,
    @SerializedName("userIdx") val userIdx: Int
)