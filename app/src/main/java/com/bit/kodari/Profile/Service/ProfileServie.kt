package com.bit.kodari.Login.Service

import android.content.Context
import android.util.Log
import com.bit.kodari.Login.Retrofit.ProfileRetrofitInterface
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Profile.Retrofit.UpdateNameView
import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileService(val context: Context) {

    private lateinit var updateNameView: UpdateNameView

    fun setUpdateNameView(updateNameView: UpdateNameView){
        this.updateNameView = updateNameView
    }
    //닉네임 변경 API 호출
    fun updateName(nickName: String, userIdx: Int){
        val updateNameService = getRetorfit().create(ProfileRetrofitInterface::class.java)

        // 다음줄 수정(jwt)
        updateNameService.updateName("jwt", userIdx, nickName).enqueue(object : Callback<UpdateNameResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<UpdateNameResponse>,
                response: Response<UpdateNameResponse>
            ) {
                updateNameView.updateNameSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<UpdateNameResponse>, t: Throwable) {      //통신 실패 했을때
                updateNameView.updateNameFailure("${t}")
            }
        })
    }

}