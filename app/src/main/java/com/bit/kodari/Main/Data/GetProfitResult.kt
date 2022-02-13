package com.bit.kodari.Main.Data

import com.google.gson.annotations.SerializedName

data class GetProfitResult(
    @SerializedName("accountIdx") val accountIdx: Int,
    @SerializedName("createAt") var createAt: String,
    @SerializedName("earning") val earning: String,
    @SerializedName("profitIdx") val profitIdx: Int,
    @SerializedName("profitRate") val profitRate: Double,
    @SerializedName("status") val status: String
)