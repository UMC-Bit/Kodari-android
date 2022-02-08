package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.NicknameResponse

interface NicknameView {
    fun getNicknameSuccess(response: NicknameResponse)
    fun getNicknameFailure(message:String)
}