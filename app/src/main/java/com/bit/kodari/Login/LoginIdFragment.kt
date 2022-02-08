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
import com.bit.kodari.databinding.FragmentLoginIdBinding

class LoginIdFragment : BaseFragment<FragmentLoginIdBinding>(FragmentLoginIdBinding::inflate) {

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        binding.loginIdNextBtn.setOnClickListener {
            if(binding.loginIdEt.text != null){
                (context as LoginActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl, LoginPwFragment().apply {
                        arguments = Bundle().apply {
                            putString("email", binding.loginIdEt.text.toString())
                        }
                    }).addToBackStack(null).commitAllowingStateLoss()
            }else{
                showToast("이메일을 입력해주세요.")
            }

        }
    }
}