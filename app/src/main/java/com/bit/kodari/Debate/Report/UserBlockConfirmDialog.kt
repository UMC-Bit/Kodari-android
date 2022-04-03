package com.bit.kodari.Debate.Report

import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogUserBlockConfirmBinding

class UserBlockConfirmDialog(val idx:Int) : BaseDialogFragment<DialogUserBlockConfirmBinding>(DialogUserBlockConfirmBinding::inflate) {
    override fun initAfterBinding() {
        setListener()
    }

    private fun setListener() {
        binding.userBlockDialogDeleteBtn.setOnClickListener {
            //api 호출
        }
        binding.userBlockDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }
}