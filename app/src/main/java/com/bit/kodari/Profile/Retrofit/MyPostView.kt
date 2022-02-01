package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.GetMyPostResponse


interface MyPostView {
    fun getMyPostSuccess(response: GetMyPostResponse)
    fun getMyPostFailure(message:String)
}