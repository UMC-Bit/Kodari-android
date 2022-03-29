package com.bit.kodari.Debate.Report

import android.util.Log
import androidx.core.os.bundleOf
import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogReportMenuBinding

class ReportMenuDialog(val Idx: Int, val flag: Int) :
    BaseDialogFragment<DialogReportMenuBinding>(DialogReportMenuBinding::inflate) {
    override fun initAfterBinding() {
        setListener()

        //신고가 완료되었다고 오면
        requireActivity().supportFragmentManager.setFragmentResultListener("request", this,
            { key, bundle ->
                if (key == "request") {
                    if (bundle.containsKey("success")) {       //삭제되었다고 하고 넘어오면
                        Log.d("신고 닫기" ,"${bundle.toString()}")
                        val bundle = bundleOf("success2" to true) //삭제되었다고 보내기
                        requireActivity().supportFragmentManager.setFragmentResult(
                            "request2",
                            bundle
                        )
                        dismiss()
                    }
                }
            })
    }

    fun setListener() {
        //상업적 판매
        binding.dialogReportMenuContentSellTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx, 1, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportReasonSellDialog")
        }

        //욕설 비하
        binding.dialogReportMenuContentAbuseTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx, 2, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportReasonAbuseDialog")
        }

        //성격 부적절 주제 부적절
        binding.dialogReportMenuContentTopicTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx, 3, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportReasonTopicDialog")
        }


        //음란물
        binding.dialogReportMenuContentPornoTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx, 4, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportReasonPornoDialog")
        }


        //낚시 도배
        binding.dialogReportMenuContentSpamTV.setOnClickListener {
            val dialog = ReportReasonDialog(Idx, 5, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportReasonSpamDialog")
        }

        //취소하기
        binding.dialogReportMenuCancelTv.setOnClickListener {
            dismiss()
        }
    }
}