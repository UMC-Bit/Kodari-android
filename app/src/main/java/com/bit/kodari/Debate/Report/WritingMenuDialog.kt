package com.bit.kodari.Debate.Report

import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogWritingMenuBinding

class WritingMenuDialog(val Idx : Int , val flag :Int)  : BaseDialogFragment<DialogWritingMenuBinding>(DialogWritingMenuBinding::inflate){

    override fun initAfterBinding() {
        setListener()

    }

    fun setListener(){
        //신고하기 눌렀을때
        binding.dialogWritingMenuContentTV.setOnClickListener {
            val dialog = ReportMenuDialog(Idx , flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportMenuDialog")
        }

        binding.dialogWritingMenuCancelTv.setOnClickListener {
            dismiss()
        }
    }
}