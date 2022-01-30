package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupPwBinding

class SignupPwFragment : Fragment() , SignUpView {

    lateinit var binding: FragmentSignupPwBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupPwBinding.inflate(inflater, container, false)

        var test = SignUpInfo("test2", "test3154@naver.com" , "a1234@1234")

        binding.signupPwNextBtn.setOnClickListener {

            val logInService = LogInService(requireActivity())
            logInService.setSignUpView(this)
            logInService.getSignUp(test)

        }

        return binding.root
    }

    override fun signUpSuccess(resp: SignUpResponse) {
        when(resp.code){
            1000 -> {
                Toast.makeText(context,"회원가입 성공" , Toast.LENGTH_SHORT).show()
                (context as LoginActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl, SignupNicknameFragment()).commitAllowingStateLoss()
            }
            else -> {
                binding.signupPwInfoTv.text = "${resp.message}"
                Toast.makeText(context,"회원가입 실패 , ${resp.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun signUpFailure(msg:String) {
        binding.signupPwInfoTv.text = msg
    }
}