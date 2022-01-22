package com.bit.kodari.PossessionCoin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinAddBinding
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinAddFragment : Fragment() {
    lateinit var binding:FragmentPossessionCoinAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinAddBinding.inflate(inflater, container, false)

        binding.possessionCoinAddCompleteButtonTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinAddBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}