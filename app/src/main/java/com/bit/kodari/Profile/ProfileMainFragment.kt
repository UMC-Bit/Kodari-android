package com.bit.kodari.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.ProfileMainView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentProfileMainBinding

class ProfileMainFragment: BaseFragment<FragmentProfileMainBinding>(FragmentProfileMainBinding::inflate) , ProfileMainView{


    lateinit var nickName:String
    lateinit var email:String

    override fun initAfterBinding() {

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
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, EditProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString("nickName", tempNickName)
                        putString("email",tempEmail)
                    }
                }).commitAllowingStateLoss()
        }

        binding.profileMainBtn2Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, MyNewsFragment()).commitAllowingStateLoss()
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
        binding.profileMainNameTv.text = nickName
        binding.profileMainEmailTv.text = email
        Log.d("getprofile" , "닉네임 : ${nickName} , 이메일 : ${email}")
        dismissLoadingDialog()
    }

    override fun getProfileFailure(message: String) {
        Log.d("getProfile" , "호출 실패 , ${message}" )
        dismissLoadingDialog()
    }
}