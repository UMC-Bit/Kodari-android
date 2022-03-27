package com.bit.kodari.Debate.Report

import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogWritingMenuBinding

class WritingMenuDialog(val Idx : Int , val flag :Int )  : BaseDialogFragment<DialogWritingMenuBinding>(DialogWritingMenuBinding::inflate){

    override fun initAfterBinding() {
        setInit()
        setListener()

    }

    private fun setInit() {
        if(flag == 2){
            binding.dialogWritingMenuContentTV.text = "댓글 신고하기"
        } else if(flag == 3){
            binding.dialogWritingMenuContentTV.text = "답글 신고하기"
        }
    }

    //div가 1, 2, 3에 따라서 게시글 ,댓글 ,답글
    private fun setListener(){
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