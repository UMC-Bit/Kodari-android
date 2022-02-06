package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.PostData.DebateWritePostResponse
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse

interface DebatePostWriteVIew {
    fun updatePostSuccess(response:DebateWritePostResponse)
    fun updatePostFailure(message:String)
    fun getUserInfoSuccess(response: GetProfileResponse)           //유저 정보 받아오는 것과 프로필 정보 받아오는것의 결과가 같음
    fun getUserInfoFailure(message: String)
}