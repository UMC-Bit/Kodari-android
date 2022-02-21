package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class GetTradeListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<GetTradeListResult>
)