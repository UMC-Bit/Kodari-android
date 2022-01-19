package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSignupIdCheckBinding

class SignupIdCheckFragment : Fragment() {

    lateinit var binding: FragmentSignupIdCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupIdCheckBinding.inflate(inflater , container , false)

        binding.signupIdCheckNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupPwFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}