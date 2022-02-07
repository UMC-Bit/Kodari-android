package com.bit.kodari.PossessionCoin.Retrofit

import com.bit.kodari.Debate.PostData.DebateDeletePostResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtDeleteResponse

interface PsnCoinMgtDeleteView {
    fun deletePsnCoinSuccess(response: PsnCoinMgtDeleteResponse)
    fun deletePsnCoinFailure(message:String)
}