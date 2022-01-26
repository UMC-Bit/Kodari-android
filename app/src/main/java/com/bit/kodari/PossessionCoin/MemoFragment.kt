package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentEditPwBinding
import com.bit.kodari.databinding.FragmentMemoBinding

class MemoFragment : Fragment() {
    lateinit var binding: FragmentMemoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        return binding.root
    }


}