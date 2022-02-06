package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.GetMyCommentResponse

interface MyCommentView {
    fun getMyCommentSuccess(response: GetMyCommentResponse)
    fun getMyCommentFailure(message:String)
}