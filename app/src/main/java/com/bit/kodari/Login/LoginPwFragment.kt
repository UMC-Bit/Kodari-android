package com.bit.kodari.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.LogInView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.Util.getAutoLogin
import com.bit.kodari.Util.saveAutoLogin
import com.bit.kodari.Util.saveLoginInfo
import com.bit.kodari.databinding.FragmentLoginPwBinding

class LoginPwFragment : BaseFragment<FragmentLoginPwBinding>(FragmentLoginPwBinding::inflate) , LogInView {

    private lateinit var email :String
    private lateinit var pw :String
    private lateinit var callback:OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl , LoginIdFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }
    override fun initAfterBinding() {
        getEmail()
        setInit()
        setListener()
    }

    fun setInit(){
        Log.d("AutoLogin" , "${getAutoLogin()}")
        binding.loginPwAutoLoginCb.isChecked = getAutoLogin()
    }


    fun getEmail(){
        if(requireArguments().containsKey("email")){
            email = requireArguments().getString("email")!!
            Log.d("init" , "${email}")
        }
    }

    override fun logInSuccess(response: LogInResponse) {
        dismissLoadingDialog()
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
        dismissLoadingDialog()
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
                showLoadingDialog(requireContext())
                logInService.getLogIn(userInfo)
            }
        }

        binding.loginPwAutoLoginCb.setOnClickListener {
            saveAutoLogin(binding.loginPwAutoLoginCb.isChecked)
        }

        binding.loginPwBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , LoginIdFragment()).commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}