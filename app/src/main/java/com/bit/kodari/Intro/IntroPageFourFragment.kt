package com.bit.kodari.Intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.LoginFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentIntroPageFourBinding

class IntroPageFourFragment : Fragment() {

    lateinit var binding : FragmentIntroPageFourBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroPageFourBinding.inflate(inflater, container, false)

        binding.introPageFourStartIv.setOnClickListener{
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}