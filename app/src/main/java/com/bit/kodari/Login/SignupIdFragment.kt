package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupIdBinding

class SignupIdFragment : BaseFragment<FragmentSignupIdBinding>(FragmentSignupIdBinding::inflate){

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        binding.signupIdNextBtn.setOnClickListener {
            val email = binding.signupIdEt.text.toString()
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupPwFragment().apply {
                    arguments = Bundle().apply {
                        putString("email" , email)
                    }
                }).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.signupIdXIb.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginFragment()).addToBackStack(null).commitAllowingStateLoss()
        }
    }
}