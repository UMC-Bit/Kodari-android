package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentLoginIdBinding

class LoginIdFragment : Fragment() {

    lateinit var binding:FragmentLoginIdBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginIdBinding.inflate(inflater, container, false)

        binding.loginIdNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginPwFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}