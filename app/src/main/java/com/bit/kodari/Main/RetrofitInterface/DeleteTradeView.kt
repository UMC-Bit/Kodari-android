package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.PossessionCoin.RetrofitData.DeleteTradeResponse

interface DeleteTradeView {
    fun deleteTradeSuccess(response: DeleteTradeResponse)
    fun deleteTradeFailure(message: String)
}