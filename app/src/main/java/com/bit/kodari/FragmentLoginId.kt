package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentLoginIdBinding

class FragmentLoginId : Fragment() {

    lateinit var binding:FragmentLoginIdBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginIdBinding.inflate(inflater, container, false)

        binding.loginIdNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentLoginPw()).commitAllowingStateLoss()
        }

        return binding.root
    }
}