package com.bit.kodari.Portfolio

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentEnrollExchangeBinding


class EnrollExchangeFragment : Fragment() {

    private lateinit var binding : FragmentEnrollExchangeBinding
    private lateinit var callback : OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , HomeFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

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
                }).commit()
        }

        binding.enrollBackBtnIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , HomeFragment()).commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}