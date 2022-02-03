package com.bit.kodari.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.LogInView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.Util.saveLoginInfo
import com.bit.kodari.databinding.FragmentLoginPwBinding

class LoginPwFragment : BaseFragment<FragmentLoginPwBinding>(FragmentLoginPwBinding::inflate) , LogInView {

    private lateinit var email :String
    private lateinit var pw :String

    override fun initAfterBinding() {
        if(!arguments?.isEmpty!!){
            email = requireArguments().getString("email")!!
            Log.d("init" , "${email}")
        }
        setListener()
    }

    override fun logInSuccess(response: LogInResponse) {
        when(response.code){
            1000->{
                val jwt = response.result.jwt
                val userIdx = response.result.userIdx
                saveLoginInfo(jwt , email,pw,userIdx)
                Log.d("jwt" , "${jwt} 와 ${userIdx}")
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
            else -> {showToast(response.message)}
        }
    }

    override fun logInFailure(message: String) {
        showToast("로그인 실패")
    }

    fun setListener(){
        binding.loginPwNextBtn.setOnClickListener {
            if(binding.loginPwEt.text.isEmpty()){
                showToast("비밀번호를 설정해주세요.")
            } else {
                pw = binding.loginPwEt.text.toString()
                Log.d("email" ,"Password에서 email 셋팅 2 , ${email}")
                var userInfo = LogInInfo(email, pw)
                val logInService = LogInService()
                logInService.setLogInView(this)
                logInService.getLogIn(userInfo)
            }
        }
    }
}