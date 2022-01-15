package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentPossessionCoinDialogBinding
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinDialogFragment : Fragment(){
    lateinit var binding:FragmentPossessionCoinDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPossessionCoinDialogBinding.inflate(inflater , container , false)

        binding.possessionCoinDialogMemoOffTV.setOnClickListener {
            binding.possessionCoinDialogMemoOnTV.visibility=View.VISIBLE
            binding.possessionCoinDialogTwitterOnTV.visibility=View.GONE
        }

        binding.possessionCoinDialogTwitterOffTV.setOnClickListener {
            binding.possessionCoinDialogTwitterOnTV.visibility=View.VISIBLE
            binding.possessionCoinDialogMemoOnTV.visibility=View.GONE
            binding.possessionCoinDialogMemoOffTV.visibility=View.VISIBLE
        }

        return binding.root
    }
}