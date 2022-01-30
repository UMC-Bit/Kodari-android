package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse


interface UpdateNameView {
    fun updateNameSuccess(resp: UpdateNameResponse)         //닉네임 변경 API 통신 성공했을 때
    fun updateNameFailure(message:String)         //닉네임 변경 API 통신 실패했을 때
}