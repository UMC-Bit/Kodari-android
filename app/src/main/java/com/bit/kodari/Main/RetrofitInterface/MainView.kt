package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Profile.RetrofitData.GetProfileResponse

interface MainView {
    fun getUserSuccess(resp:GetProfileResponse)
    fun getUserFailure(message:String)
}