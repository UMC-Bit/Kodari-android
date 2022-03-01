package com.bit.kodari.Util.Coin.USD

import com.bit.kodari.Portfolio.Data.SearchCoinResult
import com.google.gson.annotations.SerializedName

data class UsdResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<UsdResult>
)