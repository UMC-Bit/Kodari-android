package com.bit.kodari.PossessionCoin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding
import com.bit.kodari.databinding.FragmentPossessionCoinModifyBinding

class PossessionCoinModifyFragment : Fragment() {
    lateinit var binding:FragmentPossessionCoinModifyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinModifyBinding.inflate(inflater, container, false)

        binding.possessionCoinModifyCompleteButtonTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.temp, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinModifyBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.temp, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }


        binding.possessionCoinModifyBuyOffTV.setOnClickListener {
            binding.possessionCoinModifyBuyOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifySellOnTV.visibility=View.GONE
        }

        binding.possessionCoinModifySellOffTV.setOnClickListener {
            binding.possessionCoinModifySellOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifyBuyOnTV.visibility=View.GONE
        }

        return binding.root
    }
}