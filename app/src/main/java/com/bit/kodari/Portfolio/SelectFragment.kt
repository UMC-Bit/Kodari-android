package com.bit.kodari.Portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentSelectBinding


class SelectFragment : BaseFragment<FragmentSelectBinding>(FragmentSelectBinding::inflate){

    override fun initAfterBinding() {
        setListener()
    }

    fun setListener() {
        binding.selectBackBtnIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, SelectFragment()).commitAllowingStateLoss()
        }
    }




}