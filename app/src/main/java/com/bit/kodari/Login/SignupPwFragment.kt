package com.bit.kodari.Login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.PasswordView
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.*
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupPwBinding

class SignupPwFragment : BaseFragment<FragmentSignupPwBinding>(FragmentSignupPwBinding::inflate), PasswordView {

    lateinit var email : String
    lateinit var pw : String

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        binding.signupPwNextBtn.setOnClickListener {
            val logInService = LogInService()
            logInService.setPasswordView(this)
            logInService.getCheckPassword(PasswordInfo(binding.signupPwEt.text.toString()))
        }

        binding.signupPwXIb.setOnClickListener{
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginFragment()).commitAllowingStateLoss()
        }
    }

    override fun getPasswordSuccess(response: PasswordResponse) {
        when(response.code){
            1000-> {
                Toast.makeText(context, "Password 입력 성공", Toast.LENGTH_SHORT).show()
                pw = binding.signupPwEt.text.toString()
                email = requireArguments().getString("email")!!
                (context as LoginActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl, SignupNicknameFragment().apply {
                        arguments = Bundle().apply {
                            putString("email" , email)
                            putString("pw",pw)
                        }
                    }).addToBackStack(null).commitAllowingStateLoss()
            }
            else -> {
                binding.signupPwCheckTv.text = response.message
            }
        }
    }

    override fun getPasswordFailure(message: String) {
        showToast("Password Check 실패 ,$message")
    }
}