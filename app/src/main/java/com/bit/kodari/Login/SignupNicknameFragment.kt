package com.bit.kodari.Login

import android.os.Bundle
import android.util.Log
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
import com.bit.kodari.databinding.FragmentSignupNicknameBinding

class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(FragmentSignupNicknameBinding::inflate) ,SignUpView {

    private lateinit var email :String
    private lateinit var pw: String
    private lateinit var nickName: String

    override fun initAfterBinding() {
        if(!arguments?.isEmpty!!){
            //이전 프래그먼트에서 값 없으면 못 넘어오게해야함
            email = requireArguments().getString("email")!!
            pw = requireArguments().getString("pw")!!
        }
        setListener()
    }

    fun setListener(){
        binding.signupNicknameNextBtn.setOnClickListener {
            nickName = binding.signupNicknameEt.text.toString()
            val signUpInfo = SignUpInfo(nickName,email,pw)
            Log.d("signup " ,"정보 : ${nickName} , ${email} , ${pw}")
            val logInService = LogInService()
            logInService.setSignUpView(this)
            logInService.getSignUp(signUpInfo)
        }

        binding.signupNicknameXIb.setOnClickListener{
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupPwFragment()).commitAllowingStateLoss()
        }
    }

    override fun signUpSuccess(resp: SignUpResponse) {
        when(resp.code){
            1000 -> {
                Toast.makeText(context,"회원가입 성공" , Toast.LENGTH_SHORT).show()
                (context as LoginActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container_fl, LoginFragment()).commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context,"회원가입 실패 , ${resp.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun signUpFailure(msg:String) {
        showToast("회원가입 실패 ,$msg")
    }
}