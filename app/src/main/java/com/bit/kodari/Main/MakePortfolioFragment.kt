package com.bit.kodari.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Portfolio.BottomSheetFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentMakePortfolioBinding

class MakePortfolioFragment : Fragment() {

    lateinit var binding: FragmentMakePortfolioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakePortfolioBinding.inflate(inflater , container , false)
        setListener()
        return binding.root
    }

    fun setListener(){
        binding.makePortfolioMakeBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"BottomSheetDialog")
        }

    }
}