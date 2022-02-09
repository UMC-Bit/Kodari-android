package com.bit.kodari.Login.Retrofit

import com.bit.kodari.Login.RetrofitData.NicknameInfo
import com.bit.kodari.Login.RetrofitData.NicknameResponse
import com.bit.kodari.Profile.RetrofitData.*
import retrofit2.Call
import retrofit2.http.*


interface ProfileRetrofitInterface {

    // 회원 목록 유저인덱스로 조회
    @GET("/app/users/get")
    fun getProfile(@Query("userIdx") userIdx:Int) :Call<GetProfileResponse>


    // 회원 닉네임 변경
    @PATCH("/app/users/update/nickName/{userIdx}")
    fun updateName(@Header("X-ACCESS-TOKEN") jwt:String, @Path("userIdx") userIdx:Int,
                   @Query("nickName") nickName:String) : Call<UpdateNameResponse>

    // 회원 프로필 사진 변경
    @PATCH("app/users/update/profileImgUrl/{userIdx}")
    fun updateProfileImg(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int,
    @Query("profileImgUrl") profileImgUrl:String) : Call<UpdateProfileImgResponse>

    // 토론장 유저 게시글 조회 (내 글 모아보기)
    @GET("/posts")
    fun getMyPost(@Query("userIdx") userIdx:Int) : Call<GetMyPostResponse>

    // 토론장 게시글 유저별 댓글 조회 (내 댓글 모아보기)
    @GET("/comments/user")
    fun getMyComment(@Query("userIdx") userIdx: Int) : Call<GetMyCommentResponse>

    // 닉네임 validation 확인
    @POST("/app/users/get/getCheckNickName")
    fun getCheckNickname(@Body nicknameInfo: NicknameInfo) : Call<NicknameResponse>

    // 현재 비밀번호 일치 확인
    @POST("/app/users/get/checkPassword/{userIdx}")
    fun checkPassword(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int,
                      @Body checkPasswordInfo: CheckPasswordInfo) : Call<CheckPasswordResponse>

    // 비밀번호 변경
    @PATCH("/app/users/update/password/{userIdx}")
    fun changePassword(@Header("X-ACCESS-TOKEN") jwt:String, @Path("userIdx") userIdx:Int,
                        @Body changePasswordInfo: ChangePasswordInfo) : Call<ChangePasswordResponse>

}