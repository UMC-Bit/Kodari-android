package com.bit.kodari.Debate.Report

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.databinding.DialogWritingMenuBinding

class WritingMenuDialog(val Idx: Int, val flag: Int) :
    BaseDialogFragment<DialogWritingMenuBinding>(DialogWritingMenuBinding::inflate) {

    override fun initAfterBinding() {
        setInit()
        setListener()

        //신고 되었으니 창 닫기
        requireActivity().supportFragmentManager.setFragmentResultListener("request2", this,
            { key, bundle ->
                if (key == "request2") {
                    if (bundle.containsKey("success2")) {       //삭제되었다고 하고 넘어오면 -> 이전꺼와 같으면 안됨
                        Log.d("신고 닫기1" ,"${bundle.toString()}")
                        dismiss()
                    }
                }
            })
    }

    private fun setInit() {
        if (flag == 2) {
            binding.dialogWritingMenuContentTV.text = "댓글 신고하기"
            binding.dialogWritingMenuUserBlockTV.visibility = View.GONE
            binding.dialogWritingMenuUnderLine2Iv.visibility = View.GONE
        } else if (flag == 3) {
            binding.dialogWritingMenuContentTV.text = "답글 신고하기"
            binding.dialogWritingMenuUserBlockTV.visibility = View.GONE
            binding.dialogWritingMenuUnderLine2Iv.visibility = View.GONE
        }
    }

    //div가 1, 2, 3에 따라서 게시글 ,댓글 ,답글
    private fun setListener() {
        //신고하기 눌렀을때
        binding.dialogWritingMenuContentTV.setOnClickListener {
            val dialog = ReportMenuDialog(Idx, flag)
            dialog.show(requireActivity().supportFragmentManager, "ReportMenuDialog")
        }

        binding.dialogWritingMenuUserBlockTV.setOnClickListener {
            //유저 차단하기 확인 다이얼로그 호출
            val dialog = UserBlockConfirmDialog(Idx)
            dialog.show(requireActivity().supportFragmentManager, "UserBlockConfirmDialog")
        }

        binding.dialogWritingMenuCancelTv.setOnClickListener {
            dismiss()
        }
    }
}