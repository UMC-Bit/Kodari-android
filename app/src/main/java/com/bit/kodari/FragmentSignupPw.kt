package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentSignupPwBinding

class FragmentSignupPw : Fragment() {

    lateinit var binding: FragmentSignupPwBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupPwBinding.inflate(inflater, container, false)

        binding.signupPwNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentSignupNickname()).commitAllowingStateLoss()
        }

        return binding.root
    }
}