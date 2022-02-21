package com.bit.kodari.Profile

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Intro.IntroPageOneFragment
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.ProfileMainView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentProfileMainBinding
import com.bumptech.glide.Glide

class ProfileMainFragment: BaseFragment<FragmentProfileMainBinding>(FragmentProfileMainBinding::inflate) , ProfileMainView{

    private lateinit var nickName:String
    private lateinit var email:String
    private lateinit var imgUrl:String

    override fun initAfterBinding() {
        Log.d("initAfterBinding", "실행")
        val profileService = ProfileService()
        profileService.setProfileMainView(this)
        showLoadingDialog(requireContext())
        profileService.getProfile(getUserIdx())

        setListener()

    }

    fun setListener(){
        binding.profileMainBtn1Ib.setOnClickListener {
            val tempNickName = nickName
            val tempEmail = email
            val tempUrl = imgUrl
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, EditProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString("nickName", tempNickName)
                        putString("email",tempEmail)
                        putString("url" , tempUrl)
                    }
                }).commitAllowingStateLoss()
        }

        // 뉴스 모아보기 서비스 준비 중이기 때문에 주석 처리함. 주석 지울 시에 웹뷰 연결됨
        binding.profileMainBtn2Ib.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container_fl, MyNewsFragment()).commitAllowingStateLoss()
            Toast.makeText(context, "서비스 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.profileMainBtn3Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, MyWritingFragment()).commitAllowingStateLoss()
        }

        binding.profileMainBtn4Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, MyCommentFragment()).commitAllowingStateLoss()
        }

        binding.profileMainBtn5Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, EditPwFragment()).commitAllowingStateLoss()
        }
    }

    override fun getProfileSuccess(response: GetProfileResponse) {
        nickName = response.result[0].nickName
        email = response.result[0].email
        imgUrl = response.result[0].profileImgUrl
        binding.profileMainNameTv.text = nickName
        binding.profileMainEmailTv.text = email
        Log.d("getprofile" , "닉네임 : ${nickName} , 이메일 : ${email}")
        Glide.with(binding.profileMainImageIv)
            .load(response.result[0].profileImgUrl)
            .centerCrop()
            .into(binding.profileMainImageIv)
        dismissLoadingDialog()
    }

    override fun getProfileFailure(message: String) {
        Log.d("getProfile" , "호출 실패 , ${message}" )
        dismissLoadingDialog()
    }
}