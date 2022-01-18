package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentProfileMainBinding

class ProfileMainFragment: Fragment() {

    lateinit var binding: FragmentProfileMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)

        binding.profileMainBtn2Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , MyNewsFragment()).commitAllowingStateLoss()
        }

        binding.profileMainBtn3Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , MyWritingFragment()).commitAllowingStateLoss()
        }

        binding.profileMainBtn4Ib.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , MyCommentFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}