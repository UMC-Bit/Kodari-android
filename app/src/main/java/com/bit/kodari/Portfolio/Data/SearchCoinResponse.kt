package com.bit.kodari.Portfolio.Data

import com.google.gson.annotations.SerializedName

data class SearchCoinResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<SearchCoinResult>
)