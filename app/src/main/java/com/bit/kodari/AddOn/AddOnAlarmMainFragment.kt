package com.bit.kodari.AddOn

import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentAddOnAlramMainBinding

class AddOnAlarmMainFragment : BaseFragment<FragmentAddOnAlramMainBinding>(FragmentAddOnAlramMainBinding::inflate) {

    override fun initAfterBinding() {
        setInit()
    }

    fun setInit(){
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.add_on_alram_main_rect_fl , AddOnAlarmCoinFragment()).commit()
    }

}