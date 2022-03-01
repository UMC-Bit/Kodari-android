package com.bit.kodari.Portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentEnrollExchangeBinding


class EnrollExchangeFragment : Fragment() {

    lateinit var binding : FragmentEnrollExchangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrollExchangeBinding.inflate(inflater,container, false)
        setListener()
        return binding.root
    }

    fun setListener(){
        binding.enrollUpbitIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , PortfolioManagementFragment().apply {
                    arguments = Bundle().apply {
                        //처음에 오류 없이 실행되게하기 위해서
                    }
                }).addToBackStack(null).commit()
        }

        binding.enrollBackBtnIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , HomeFragment()).commit()
        }
    }

}