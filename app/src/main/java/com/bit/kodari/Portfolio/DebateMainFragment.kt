package com.bit.kodari.Portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentDebateMainBinding

class DebateMainFragment : Fragment() {

    lateinit var binding: FragmentDebateMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDebateMainBinding.inflate(inflater , container ,false)
        return binding.root
    }

}