package com.bit.kodari.Main.Data

data class PortfolioResponse (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: PortfolioResult
)

data class PortIdxResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<PortIdxResult>
)
