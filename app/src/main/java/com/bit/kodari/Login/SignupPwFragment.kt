package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupPwBinding

class SignupPwFragment : BaseFragment<FragmentSignupPwBinding>(FragmentSignupPwBinding::inflate) {

    lateinit var email : String
    lateinit var pw : String

    override fun initAfterBinding() {
       if(!arguments?.isEmpty!!){
           email = requireArguments().getString("email")!!
       }
        setListener()
    }

    fun setListener(){

        binding.signupPwNextBtn.setOnClickListener {
            pw = binding.signupPwEt.text.toString()
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupNicknameFragment().apply {
                    arguments = Bundle().apply {
                        putString("email" , email)
                        putString("pw",pw)
                    }
                }).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.signupPwXIb.setOnClickListener{
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginFragment()).commitAllowingStateLoss()
        }
    }


}