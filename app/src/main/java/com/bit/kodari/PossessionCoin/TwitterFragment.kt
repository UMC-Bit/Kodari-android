package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentTwitterBinding

class TwitterFragment : Fragment() {
    lateinit var binding: FragmentTwitterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwitterBinding.inflate(inflater, container, false)
        return binding.root
    }
}