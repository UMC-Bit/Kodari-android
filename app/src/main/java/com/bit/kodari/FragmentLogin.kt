package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentLoginBinding

class FragmentLogin : Fragment() {

    lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater , container , false)

        // 회원 가입 버튼
        binding.loginSignUpBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentSignupId()).commitAllowingStateLoss()
        }

        // 아이디 로그인 버튼
        binding.loginSignInBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentLoginId()).commitAllowingStateLoss()
        }

        return binding.root
    }
}