package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    var anim_fade_in_3: Animation? = null

    lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater , container , false)

        // 회원 가입 버튼
        binding.loginSignUpBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupIdFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        // 아이디 로그인 버튼
        binding.loginSignInBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, LoginIdFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var signupbtn : Button = view.findViewById(R.id.login_sign_up_btn)
        var signinbtn : Button = view.findViewById(R.id.login_sign_in_btn)

        anim_fade_in_3 = AnimationUtils.loadAnimation(activity, R.anim.fade_in_3)
        signupbtn.startAnimation(anim_fade_in_3)
        signinbtn.startAnimation(anim_fade_in_3)
    }

}