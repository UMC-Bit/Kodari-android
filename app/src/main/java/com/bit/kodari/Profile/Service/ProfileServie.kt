package com.bit.kodari.Login.Service

import android.util.Log
import com.bit.kodari.Login.Retrofit.ProfileRetrofitInterface
import com.bit.kodari.Profile.Retrofit.MyPostView
import com.bit.kodari.Profile.Retrofit.ProfileEditView
import com.bit.kodari.Profile.Retrofit.ProfileMainView
import com.bit.kodari.Profile.RetrofitData.GetMyPostResponse
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Profile.RetrofitData.UpdateProfileImgResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import com.bit.kodari.Util.getUserIdx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileService() {

    private lateinit var profileEditView: ProfileEditView
    private lateinit var profileMainView: ProfileMainView
    private lateinit var myPostView: MyPostView

    fun setProfileEditView(profileEditView: ProfileEditView){
        this.profileEditView = profileEditView
    }

    fun setProfileMainView(profileMainView: ProfileMainView){
        this.profileMainView = profileMainView
    }

    fun setMyPostView(myPostView: MyPostView){
        this.myPostView = myPostView
    }

    // 닉네임 변경 API 호출
    fun updateName(nickName: String) {
        val updateNameService = getRetorfit().create(ProfileRetrofitInterface::class.java)

        Log.d("updateName" , "${getJwt()} ,${getUserIdx()} , ${nickName}")
        updateNameService.updateName(getJwt()!!, getUserIdx(), nickName).enqueue(object : Callback<UpdateNameResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<UpdateNameResponse>,
                response: Response<UpdateNameResponse>
            ) {
                Log.d("updatename" , "성공")
                if(response.body() == null){
                    Log.d("updateName" , "null")
                }

                profileEditView.updateNameSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<UpdateNameResponse>, t: Throwable) {      //통신 실패 했을때
                Log.d("updatename" , "실패")
                profileEditView.updateNameFailure("${t}")
            }
        })
    }

    // 프로필 사진 변경 API 호출
    fun updateProfileImg(userIdx: Int, profileImgUrl: String) {
        val updateProfileImgService = getRetorfit().create(ProfileRetrofitInterface::class.java)

        updateProfileImgService.updateProfileImg("jwt", userIdx, profileImgUrl).enqueue(object : Callback<UpdateProfileImgResponse>{

            override fun onResponse(
                call: Call<UpdateProfileImgResponse>,
                response: Response<UpdateProfileImgResponse>
            ) {
                profileEditView.updateProfileImgSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<UpdateProfileImgResponse>, t: Throwable) {
                profileEditView.updateProfileImgFailure("${t}")
            }
        })
    }

    // 유저 정보 유저인덱스로 조회하는 API 호출
    fun getProfile(userIdx:Int){
        val profileService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        profileService.getProfile(userIdx).enqueue(object : Callback<GetProfileResponse>{
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                profileMainView.getProfileSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                profileMainView.getProfileFailure("${t}")
            }
        })
    }

    // 유저 게시글 조회 API (내 글 모아보기)
    fun getMyPost(userIdx: Int) {
        val getMyPostService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        getMyPostService.getMyPost(userIdx).enqueue(object : Callback<GetMyPostResponse>{
            override fun onResponse(
                call: Call<GetMyPostResponse>,
                response: Response<GetMyPostResponse>
            ) {
                myPostView.getMyPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<GetMyPostResponse>, t: Throwable) {
                myPostView.getMyPostFailure("${t}")
            }
        })
    }



}