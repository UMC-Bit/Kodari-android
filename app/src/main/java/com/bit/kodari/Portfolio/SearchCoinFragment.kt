package com.bit.kodari.Portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSearchCoinBinding

class SearchCoinFragment : Fragment() {

    lateinit var binding: FragmentSearchCoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchCoinBinding.inflate(inflater,container,false)
        setListener()
        return binding.root
    }

    fun setListener(){
        binding.searchCoinSearchBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , SelectFragment()).addToBackStack(null).commit()
        }
    }

}