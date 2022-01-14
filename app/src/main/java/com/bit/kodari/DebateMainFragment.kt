package com.bit.kodari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentDebateBinding

class DebateMainFragment : Fragment() {

    lateinit var binding: FragmentDebateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDebateBinding.inflate(inflater , container ,false)
        return binding.root
    }

}