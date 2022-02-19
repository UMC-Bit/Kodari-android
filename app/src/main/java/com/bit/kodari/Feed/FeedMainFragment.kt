package com.bit.kodari.Feed

import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentFeedMainBinding

class FeedMainFragment : BaseFragment<FragmentFeedMainBinding>(FragmentFeedMainBinding::inflate) {
    override fun initAfterBinding() {
        binding.feedMainBackbuttonIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commitAllowingStateLoss()
        }
    }
}