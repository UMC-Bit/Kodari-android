package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupNicknameBinding

class SignupNicknameFragment : Fragment() {
    lateinit var binding: FragmentSignupNicknameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupNicknameBinding.inflate(inflater, container, false)

        binding.signupNicknameNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}