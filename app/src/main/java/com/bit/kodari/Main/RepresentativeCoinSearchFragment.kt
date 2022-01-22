package com.bit.kodari.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentRepresentativeCoinSearchBinding

class RepresentativeCoinSearchFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeCoinSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeCoinSearchBinding.inflate(inflater , container , false)

        binding.representativeCoinSearchBackIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.representativeCoinSearchCompleteTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}