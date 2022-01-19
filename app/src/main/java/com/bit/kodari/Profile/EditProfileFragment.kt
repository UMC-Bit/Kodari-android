package com.bit.kodari.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.editProfilePreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }


}