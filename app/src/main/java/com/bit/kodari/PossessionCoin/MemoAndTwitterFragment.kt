package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinViewpagerAdapter
import com.bit.kodari.databinding.FragmentMemoAndTwitterBinding
import com.google.android.material.tabs.TabLayoutMediator

class MemoAndTwitterFragment : Fragment(){

    lateinit var binding: FragmentMemoAndTwitterBinding
    private val information = arrayListOf("메모", "트위터")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMemoAndTwitterBinding.inflate(inflater , container , false)

        val possessionCoinAdapter = PossessionCoinViewpagerAdapter(this)
        binding.memoAndTwitterVp.adapter = possessionCoinAdapter
        TabLayoutMediator(binding.memoAndTwitterTb, binding.memoAndTwitterVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }
}