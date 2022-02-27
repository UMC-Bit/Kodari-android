package com.bit.kodari.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.ProfileMainView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.Util.saveAutoLogin
import com.bit.kodari.Util.saveLoginInfo
import com.bit.kodari.databinding.FragmentProfileMainBinding
import com.bumptech.glide.Glide

class ProfileMainFragment: BaseFragment<FragmentProfileMainBinding>(FragmentProfileMainBinding::inflate) , ProfileMainView{

    private lateinit var nickName:String
    private lateinit var email:String
    private var imgUrl:String? = ""

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
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container_fl, EditProfileActivity().apply {
//                    arguments = Bundle().apply {
//                        putString("nickName", tempNickName)
//                        putString("email",tempEmail)
//                        putString("url" , tempUrl)
//                    }
//                }).commitAllowingStateLoss()
            //프로필 편집 Activity실행
            val intent = Intent(requireContext(), EditProfileActivity()::class.java)
            intent.putExtra("nickName" , tempNickName)
            intent.putExtra("email" , tempEmail)
            intent.putExtra("url",tempUrl)
            startActivity(intent)
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
        binding.profileMainBtn6Ib.setOnClickListener { // 플러스 친구
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://plus.kakao.com/home/@kodari"))
            startActivity(intent)
        }

        binding.profileMainLogoutBtn.setOnClickListener {
            saveLoginInfo(null, null, null, 0)     //0이면 유저 없는거
            saveAutoLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
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
            .error(R.drawable.ic_basic_profile)
            .into(binding.profileMainImageIv)
        dismissLoadingDialog()
    }

    override fun getProfileFailure(message: String) {
        Log.d("getProfile" , "호출 실패 , ${message}" )
        dismissLoadingDialog()
    }
}