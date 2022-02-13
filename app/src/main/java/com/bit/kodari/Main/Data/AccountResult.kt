package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class AccountResult(
    @SerializedName("accountIdx") val accountIdx: Int,
    @SerializedName("accountName") val accountName: String,
    @SerializedName("property") val property: Double,
    @SerializedName("totalProperty") val totalProperty: Double,
    @SerializedName("userIdx") val userIdx: Int,
    @SerializedName("marketName") val marketName: String
)
