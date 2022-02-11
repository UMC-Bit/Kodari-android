package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.google.gson.annotations.SerializedName

data class RptCoinMgtInsquireResponse(
    @SerializedName("code")val code: Int,
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: ArrayList<RptCoinMgtInsquireResult>
)