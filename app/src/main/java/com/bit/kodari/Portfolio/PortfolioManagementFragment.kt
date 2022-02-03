package com.bit.kodari.Portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPortfolioManagementBinding

class PortfolioManagementFragment : BaseFragment<FragmentPortfolioManagementBinding>(FragmentPortfolioManagementBinding::inflate) {

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        binding.portfolioManagementCoinBackBtnIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , EnrollExchangeFragment()).addToBackStack(null).commit()
        }

        binding.portfolioManagementSearchCoinBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , PortfolioSearchFragment()).commit()
        }
    }


}