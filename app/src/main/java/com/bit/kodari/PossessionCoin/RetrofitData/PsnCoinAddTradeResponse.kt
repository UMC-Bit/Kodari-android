package com.bit.kodari.PossessionCoin.RetrofitData

import com.google.gson.annotations.SerializedName

// example response 부분의 body(to json 플러그인을 통해 생성하면 result도 자동 생성됨)
data class PsnCoinAddTradeResponse(
    @SerializedName("code")val code: Int,
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: PsnCoinAddTradeResult
)