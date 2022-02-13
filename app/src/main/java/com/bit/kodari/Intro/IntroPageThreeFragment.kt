package com.bit.kodari.Intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentIntroPageThreeBinding

class IntroPageThreeFragment : Fragment() {
    lateinit var binding : FragmentIntroPageThreeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroPageThreeBinding.inflate(inflater, container, false)

        return binding.root
    }
}