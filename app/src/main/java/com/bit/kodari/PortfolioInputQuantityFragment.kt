package com.bit.kodari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentPortfolioInputQuantityBinding


class PortfolioInputQuantityFragment : Fragment() {

    lateinit var binding : FragmentPortfolioInputQuantityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPortfolioInputQuantityBinding.inflate(inflater , container , false)
        return binding.root
    }

}