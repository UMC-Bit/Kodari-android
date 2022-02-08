package com.bit.kodari.Profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.RetrofitData.NicknameInfo
import com.bit.kodari.Login.RetrofitData.NicknameResponse
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Login.SignupPwFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.ProfileEditView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Profile.RetrofitData.UpdateProfileImgResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentEditProfileBinding

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) , ProfileEditView {

    lateinit var nickName:String

    override fun initAfterBinding() {
        setInit()
        setListener()

    }

    fun setInit(){
        if(requireArguments().containsKey("nickName")){
            nickName = requireArguments().getString("nickName")!!
            binding.editProfileInputNicknameEt.setText(nickName)
        }

    }

    override fun updateNameSuccess(resp: UpdateNameResponse) {
        showToast("닉네임 변경 성공")
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container_fl, ProfileMainFragment()).commitAllowingStateLoss()

    }

    override fun updateNameFailure(message: String) {
        Log.d("updateName" , "변경 실패")
    }

    override fun updateProfileImgSuccess(resp: UpdateProfileImgResponse) {

    }

    override fun updateProfileImgFailure(message: String) {

    }


    fun setListener(){
        binding.editProfilePreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).commitAllowingStateLoss()
        }

        binding.editProfileFinishB.setOnClickListener {

            val nickName = binding.editProfileInputNicknameEt.text.toString()
            val profileService = ProfileService()
            profileService.setProfileEditView(this)
            profileService.updateName(nickName)
        }

        binding.editProfileDoubleCheckB.setOnClickListener {
            val profileService = ProfileService()
            profileService.setProfileEditView(this)
            profileService.getCheckNickname(NicknameInfo(binding.editProfileInputNicknameEt.text.toString()))
        }

    }

    override fun getCheckNicknameSuccess(response: NicknameResponse) {
        when(response.code){
            1000-> {
                Toast.makeText(context, "닉네임 입력 성공", Toast.LENGTH_SHORT).show()
                binding.editProfileErrorTv.text = response.result
            }
            else -> {
                binding.editProfileErrorTv.text = response.message
            }
        }
    }

    override fun getCheckNicknameFailure(message: String) {
        showToast("Nickname Check 실패 ,$message")
    }
}