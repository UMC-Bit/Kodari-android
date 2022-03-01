package com.bit.kodari.Debate.PostData

import com.google.gson.annotations.SerializedName

data class DebateWritePostResult(
    @SerializedName("userIdx") val userIdx: Int
)