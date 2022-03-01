package com.bit.kodari.RepresentativeCoin.RetrofitData

import com.google.gson.annotations.SerializedName

data class RptCoinSearchResponse(
    @SerializedName("code")val code: Int,
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: List<RptCoinSearchResult>
)