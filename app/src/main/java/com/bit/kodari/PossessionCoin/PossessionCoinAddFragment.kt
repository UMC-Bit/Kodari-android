package com.bit.kodari.PossessionCoin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentPossessionCoinAddBinding

class PossessionCoinAddFragment : Fragment() {
    lateinit var binding:FragmentPossessionCoinAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinAddBinding.inflate(inflater , container,false)

        return binding.root
    }
}