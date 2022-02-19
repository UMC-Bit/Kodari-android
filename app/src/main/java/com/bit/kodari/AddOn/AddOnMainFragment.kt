package com.bit.kodari.AddOn

import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentAddOnMainBinding

class AddOnMainFragment : BaseFragment<FragmentAddOnMainBinding>(FragmentAddOnMainBinding::inflate) {
    override fun initAfterBinding() {
        binding.addonMainBackbuttonIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }
    }
}