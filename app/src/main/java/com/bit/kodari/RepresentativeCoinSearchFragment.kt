package com.bit.kodari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.databinding.FragmentRepresentativeCoinAddBinding
import com.bit.kodari.databinding.FragmentRepresentativeCoinMainBinding

class RepresentativeCoinSearchFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeCoinAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }
}