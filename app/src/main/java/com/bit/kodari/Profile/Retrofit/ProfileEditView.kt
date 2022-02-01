package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Profile.RetrofitData.UpdateProfileImgResponse


interface ProfileEditView {
    fun updateNameSuccess(resp: UpdateNameResponse)         //닉네임 변경 API 통신 성공했을 때
    fun updateNameFailure(message:String)         //닉네임 변경 API 통신 실패했을 때
    fun updateProfileImgSuccess(resp: UpdateProfileImgResponse)         //프로필 사진 변경 API 통신 성공했을 때
    fun updateProfileImgFailure(message:String)         //프로필 사진 변경 API 통신 실패했을 때
}