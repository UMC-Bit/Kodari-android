package com.bit.kodari.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentRepresentativeCoinMainBinding

class RepresentativeCoinMainFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeCoinMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeCoinMainBinding.inflate(inflater , container , false)
        return binding.root
    }
}