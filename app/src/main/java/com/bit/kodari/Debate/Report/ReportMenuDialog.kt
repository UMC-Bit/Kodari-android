package com.bit.kodari.Debate.Report

import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogReportMenuBinding

class ReportMenuDialog(val Idx: Int , val flag:Int) : BaseDialogFragment<DialogReportMenuBinding>(DialogReportMenuBinding::inflate) {
    override fun initAfterBinding() {
        setListener()
    }

    fun setListener(){
        //상업적 판매
        binding.dialogReportMenuContentSellTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx,1 , flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportReasonSellDialog")
        }

        //욕설 비하
        binding.dialogReportMenuContentAbuseTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx,2, flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportReasonAbuseDialog")
        }

        //성격 부적절 주제 부적절
        binding.dialogReportMenuContentTopicTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx,3, flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportReasonTopicDialog")
        }


        //음란물
        binding.dialogReportMenuContentPornoTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx,4, flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportReasonPornoDialog")
        }


        //낚시 도배
        binding.dialogReportMenuContentSpamTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx,5, flag)
            dialog.show(requireActivity().supportFragmentManager,"ReportReasonSpamDialog")
        }

        //취소하기
        binding.dialogReportMenuCancelTv.setOnClickListener {
            dismiss()
        }
    }
}