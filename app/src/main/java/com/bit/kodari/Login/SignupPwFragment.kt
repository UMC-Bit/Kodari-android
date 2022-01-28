package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Login.Retrofit.LogInService
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupPwBinding

class SignupPwFragment : Fragment() {

    lateinit var binding: FragmentSignupPwBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupPwBinding.inflate(inflater, container, false)

        var test = SignUpInfo("test1", "test@naver.com" , "a1234@1234")

        binding.signupPwNextBtn.setOnClickListener {

            val logInService = LogInService(requireActivity())
            logInService.getSignUp(test)

//            (context as LoginActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.login_container_fl, SignupNicknameFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}