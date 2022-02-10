package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class MakePortResult(
    @SerializedName("accountIdx")  val accountIdx: Int,
    @SerializedName("portIdx") val portIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)