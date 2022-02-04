package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class PsnCoinMgtInsquireResponse(
    @SerializedName("code")val code: Int,
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: ArrayList<PsnCoinMgtInsquireResult>
)