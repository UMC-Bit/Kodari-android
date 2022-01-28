package com.bit.kodari.Main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentMyPortfolioBinding


class MyPortfolioFragment : Fragment() {

    lateinit var binding : FragmentMyPortfolioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPortfolioBinding.inflate(inflater, container,false)
        setListener()
        return binding.root
    }

    fun setListener(){
        //편집 버튼 누르면 다이얼로그 띄우기
        binding.myPortfolioModifyIv.setOnClickListener {
            val dialog = ModifyInfoDialog()
            dialog.show(requireActivity().supportFragmentManager,"ModifyDialog")
        }
    }

}