package com.bit.kodari.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.databinding.FragmentLoginPwBinding

class LoginPwFragment : Fragment() {
    lateinit var binding:FragmentLoginPwBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginPwBinding.inflate(inflater , container , false)

        binding.loginPwNextBtn.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}