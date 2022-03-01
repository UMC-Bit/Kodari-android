package com.bit.kodari.Portfolio.Data

import com.google.gson.annotations.SerializedName

data class PostPortFolioResult(
    @SerializedName("accountIdx") val accountIdx: Int,
    @SerializedName("portIdx") val portIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)