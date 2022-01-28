package com.bit.kodari.Portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container,false)
        setListener()
        return binding.root
    }

    fun setListener(){
        binding.bottomSheetCloseBtn.setOnClickListener {
            dismiss()
        }

        binding.bottomSheetSelfNextBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , EnrollExchangeFragment()).addToBackStack(null).commit()
            dismiss()
        }

    }
}