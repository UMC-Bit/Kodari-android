package com.bit.kodari.Portfolio.Data

import com.google.gson.annotations.SerializedName
//포폴 만들기
data class PostPortFolioRequest(
    @SerializedName("accountIdx") val accountIdx: Int,
    @SerializedName("userIdx") val userIdx: Int
)