package com.bit.kodari.Intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentIntroPageTwoBinding

class IntroPageTwoFragment : Fragment() {

    lateinit var binding : FragmentIntroPageTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroPageTwoBinding.inflate(inflater, container, false)

        return binding.root
    }
}