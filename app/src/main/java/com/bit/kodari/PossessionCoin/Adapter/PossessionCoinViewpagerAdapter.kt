package com.bit.kodari.PossessionCoin.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bit.kodari.PossessionCoin.MemoFragment
import com.bit.kodari.PossessionCoin.TwitterFragment

class PossessionCoinViewpagerAdapter (fragment : Fragment , val coinIdx:Int) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MemoFragment(coinIdx)
            else -> TwitterFragment()
        }
    }

}