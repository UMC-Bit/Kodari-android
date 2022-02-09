package com.bit.kodari.Login.Service

import android.util.Log
import com.bit.kodari.Login.Retrofit.ProfileRetrofitInterface
import com.bit.kodari.Login.RetrofitData.NicknameInfo
import com.bit.kodari.Login.RetrofitData.NicknameResponse
import com.bit.kodari.Profile.Retrofit.*
import com.bit.kodari.Profile.RetrofitData.*
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
    private lateinit var myCommentView: MyCommentView
    private lateinit var pwEditView: PwEditView

    fun setProfileEditView(profileEditView: ProfileEditView){
        this.profileEditView = profileEditView
    }

    fun setProfileMainView(profileMainView: ProfileMainView){
        this.profileMainView = profileMainView
    }

    fun setMyPostView(myPostView: MyPostView){
        this.myPostView = myPostView
    }

    fun setMyCommentView(myCommentView: MyCommentView) {
        this.myCommentView = myCommentView
    }

    fun setPwEditView(pwEditView: PwEditView) {
        this.pwEditView = pwEditView
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

    // 토론장 게시글 유저별 댓글 조회 API (내 댓글 모아보기)
    fun getMyComment(userIdx: Int) {
        val getMyCommentService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        getMyCommentService.getMyComment(userIdx).enqueue(object : Callback<GetMyCommentResponse>{
            override fun onResponse(
                call: Call<GetMyCommentResponse>,
                response: Response<GetMyCommentResponse>
            ) {
                myCommentView.getMyCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<GetMyCommentResponse>, t: Throwable) {
                myCommentView.getMyCommentFailure("${t}")
            }
        })
    }

    // 닉네임 validation API
    fun getCheckNickname(nicknameInfo: NicknameInfo) {
        val checkNicknameService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        checkNicknameService.getCheckNickname(nicknameInfo).enqueue(object : Callback<NicknameResponse> {
            override fun onResponse(
                call: Call<NicknameResponse>,
                response: Response<NicknameResponse>
            ) {
                profileEditView.getCheckNicknameSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                profileEditView.getCheckNicknameFailure("${t}")
            }
        })
    }

    // 현재 비밀번호 일치 확인 API
    fun checkPassword(checkPasswordInfo: CheckPasswordInfo) {
        val checkPasswordService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        checkPasswordService.checkPassword(getJwt()!!, getUserIdx(),checkPasswordInfo).enqueue(object : Callback<CheckPasswordResponse> {
            override fun onResponse(
                call: Call<CheckPasswordResponse>,
                response: Response<CheckPasswordResponse>
            ) {
                pwEditView.checkPasswordSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<CheckPasswordResponse>, t: Throwable) {
                pwEditView.checkPasswordFailure("${t}")
            }

        })
    }

    // 비밀번호 변경 API
    fun changePassword(changePasswordInfo: ChangePasswordInfo) {
        val changePasswordService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        changePasswordService.changePassword(getJwt()!!, getUserIdx(), changePasswordInfo).enqueue(object : Callback<ChangePasswordResponse>{
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                pwEditView.changePasswordSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                pwEditView.changePasswordFailure("${t}")
            }
        })
    }



}