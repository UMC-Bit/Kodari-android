package com.bit.kodari.Profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.PossessionCoinSearchFragment
import com.bit.kodari.Profile.Retrofit.PwEditView
import com.bit.kodari.Profile.RetrofitData.ChangePasswordInfo
import com.bit.kodari.Profile.RetrofitData.ChangePasswordResponse
import com.bit.kodari.Profile.RetrofitData.CheckPasswordInfo
import com.bit.kodari.Profile.RetrofitData.CheckPasswordResponse
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentEditPwBinding


class EditPwFragment : BaseFragment<FragmentEditPwBinding>(FragmentEditPwBinding::inflate), PwEditView {

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, ProfileMainFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener() {
        binding.editPwPreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).commit()
        }

        binding.editPwCurrentCheckB.setOnClickListener {
            val profileService = ProfileService()
            profileService.setPwEditView(this)
            profileService.checkPassword(CheckPasswordInfo(binding.editPwInputCurrentPwEt.text.toString()))
        }

        binding.editPwFinishB.setOnClickListener {
            if(binding.editPwInputNewPwEt.text.toString().equals(binding.editPwInputCheckPwEt.text.toString())) {
                binding.editPwCorrectCheckErrorTv.text = ""
                val profileService = ProfileService()
                profileService.setPwEditView(this)
                profileService.changePassword(ChangePasswordInfo(binding.editPwInputNewPwEt.text.toString()))
            } else {
                binding.editPwCorrectCheckErrorTv.text = "비밀번호가 일치하지 않습니다"
            }
        }

    }

    override fun checkPasswordSuccess(response: CheckPasswordResponse) {
        when(response.code) {
            1000 -> {
                binding.editPwNewPwTv.visibility = View.VISIBLE
                binding.editPwInputNewPwEt.visibility = View.VISIBLE
                binding.editPwNewPwErrorTv.visibility = View.VISIBLE
                binding.editPwCheckPwTv.visibility = View.VISIBLE
                binding.editPwInputCheckPwEt.visibility = View.VISIBLE
                binding.editPwCorrectCheckErrorTv.visibility = View.VISIBLE
            }
            else -> {
                binding.editPwCurrentErrorTv.text = response.message
            }
        }
    }

    override fun checkPasswordFailure(message: String) {

    }

    override fun changePasswordSuccess(response: ChangePasswordResponse) {
        when(response.code) {
            1000 -> {
                Toast.makeText(context, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show()
                binding.editPwNewPwErrorTv.text = ""
            }
            else -> {
                binding.editPwNewPwErrorTv.text = response.message
            }
        }
    }

    override fun changePasswordFailure(message: String) {
        Toast.makeText(context, "통신 실패", Toast.LENGTH_SHORT).show()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}