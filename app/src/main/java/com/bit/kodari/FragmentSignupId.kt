package com.bit.kodari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.databinding.FragmentSignupIdBinding

class FragmentSignupId : Fragment() {

    lateinit var binding:FragmentSignupIdBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupIdBinding.inflate(inflater , container , false)

        binding.signupIdNextBtn.setOnClickListener {
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl , FragmentSignupIdCheck()).commitAllowingStateLoss()
        }
        return binding.root
    }
}