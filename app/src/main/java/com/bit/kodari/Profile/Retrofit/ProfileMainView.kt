package com.bit.kodari.Profile.Retrofit

import com.bit.kodari.Profile.RetrofitData.GetProfileResponse

interface ProfileMainView {
    fun getProfileSuccess(response: GetProfileResponse)
    fun getProfileFailure(message:String)
}