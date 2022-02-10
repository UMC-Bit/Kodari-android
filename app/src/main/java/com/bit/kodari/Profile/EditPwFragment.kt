package com.bit.kodari.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentEditPwBinding


class EditPwFragment : Fragment() {

    lateinit var binding: FragmentEditPwBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPwBinding.inflate(inflater, container, false)

        binding.editPwPreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        return binding.root
    }

}