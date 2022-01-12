package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentSignupNicknameBinding

class FragmentSignupNickname : Fragment() {
    lateinit var binding: FragmentSignupNicknameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupNicknameBinding.inflate(inflater, container, false)

        binding.signupNicknameNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentLogin()).commitAllowingStateLoss()
        }

        return binding.root
    }
}