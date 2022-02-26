package com.bit.kodari.Login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.EmailView
import com.bit.kodari.Login.RetrofitData.EmailInfo
import com.bit.kodari.Login.RetrofitData.EmailResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupIdBinding

class SignupIdFragment : BaseFragment<FragmentSignupIdBinding>(FragmentSignupIdBinding::inflate), EmailView {


    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        binding.signupIdNextBtn.setOnClickListener {
            val logInService = LogInService()
            Log.d("service 확인", "확인")
            logInService.setEmailView(this)
            logInService.getCheckEmail(EmailInfo(binding.signupIdEt.text.toString()))
        }

        binding.signupIdXIb.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginFragment()).addToBackStack(null).commitAllowingStateLoss()
        }
    }

    override fun getEmailSuccess(response: EmailResponse) {
        when(response.code){
            1000-> {
                Toast.makeText(context, "이메일 입력 성공", Toast.LENGTH_SHORT).show()
                (context as LoginActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl, SignupPwFragment().apply {
                        arguments = Bundle().apply {
                            putString("email", binding.signupIdEt.text.toString())
                        }
                    }).addToBackStack(null).commitAllowingStateLoss()
            }
            else -> {
                binding.signupIdCheckTv.text = response.message
            }
        }
    }

    override fun getEmailFailure(message: String) {
        showToast("Email Check 실패 ,$message")
    }
}