package com.bit.kodari.Intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentIntroPageOneBinding

class IntroPageOneFragment : Fragment() {
    lateinit var binding : FragmentIntroPageOneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroPageOneBinding.inflate(inflater, container, false)

        return binding.root
    }

}