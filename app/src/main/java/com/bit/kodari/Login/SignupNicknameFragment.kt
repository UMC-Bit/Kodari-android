package com.bit.kodari.Login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Retrofit.NicknameView
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.RetrofitData.NicknameInfo
import com.bit.kodari.Login.RetrofitData.NicknameResponse
import com.bit.kodari.Login.RetrofitData.SignUpInfo
import com.bit.kodari.Login.RetrofitData.SignUpResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupNicknameBinding

class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(FragmentSignupNicknameBinding::inflate) ,SignUpView, NicknameView {

    private lateinit var email :String
    private lateinit var pw: String
    private lateinit var nickName: String
    private var logInService = LogInService()
    override fun initAfterBinding() {

        setListener()
    }

    fun setListener(){
        binding.signupNicknameNextBtn.setOnClickListener {
            email = requireArguments().getString("email")!!
            pw = requireArguments().getString("pw")!!
            nickName = binding.signupNicknameEt.text.toString()
            val nickNameInfo = NicknameInfo(nickName)
            logInService.setNicknameView(this)
            logInService.getCheckNickname(nickNameInfo)
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

    override fun getNicknameSuccess(response: NicknameResponse) {


        when(response.code){
            1000-> {
                Toast.makeText(context, "Nickname 입력 성공", Toast.LENGTH_SHORT).show()
                val signUpInfo = SignUpInfo(nickName,email,pw)
                logInService.setSignUpView(this)
                logInService.getSignUp(signUpInfo)
            }
            else -> {
                binding.signupNicknameCheckTv.text = response.message
            }
        }
    }

    override fun getNicknameFailure(message: String) {
        showToast("Nickname Check 실패 ,$message")
    }
}