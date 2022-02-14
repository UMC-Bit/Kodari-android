package com.bit.kodari.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bit.kodari.databinding.DialogTermsBinding

class TermsDialog : DialogFragment() {

    lateinit var binding : DialogTermsBinding
    var check = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTermsBinding.inflate(inflater , container, false)



        return binding.root
    }

}