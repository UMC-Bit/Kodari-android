package com.bit.kodari.Intro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroViewpagerAdapter(fragment: IntroActivity) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int  = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> IntroPageOneFragment()
            1 -> IntroPageTwoFragment()
            2 -> IntroPageThreeFragment()
            else -> IntroPageFourFragment()
        }
    }
}