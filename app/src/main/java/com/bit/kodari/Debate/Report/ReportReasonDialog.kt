package com.bit.kodari.Debate.Report

import android.util.Log
import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.Debate.Report.ReportData.ReportPostRequest
import com.bit.kodari.Debate.Report.ReportData.ReportPostResponse
import com.bit.kodari.Debate.Report.ReportService.ReportService
import com.bit.kodari.Debate.Report.Retrofit.ReportPostView
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.DialogReportReasonBinding

//Sell로 넘기는데  인덱스 다르게 해서 처리해보자

class ReportReasonDialog(val postIdx: Int, val index: Int ,val flag: Int) :
    BaseDialogFragment<DialogReportReasonBinding>(DialogReportReasonBinding::inflate) , ReportPostView {
    override fun initAfterBinding() {
        setInit()
        setListener()
    }

    private fun setInit() {
        when (index) {
            1 -> {
                binding.dialogReportReasonSellTitleTV.text = "상업적 광고 및 판매 게시물"
            }
            2 -> {
                binding.dialogReportReasonSellTitleTV.text = "욕설/비하 대화 게시물"
            }
            3 -> {
                binding.dialogReportReasonSellTitleTV.text = "게시판 성격 부적절 대화 게시물"
            }
            4 -> {
                binding.dialogReportReasonSellTitleTV.text = "음란물/불건전한 대화 게시물"
            }
            5 -> {
                binding.dialogReportReasonSellTitleTV.text = "도배/낚시 대화 게시물"
            }
        }
    }

    fun setListener() {

        //확인 버튼 누르기 -> 게시글 신고
        binding.dialogReportReasonSellConfirmTV.setOnClickListener {
            if(flag == 1){
                callReport()
            } else if(flag == 2){       //댓글 신고 기능

            }else{                      //대댓 신고 기능

            }

        }

        binding.dialogReportReasonSellCancelTV.setOnClickListener {
            dismiss()
        }
    }

    private fun callReport() {
        val text = binding.dialogReportReasonSellTitleTV.text.toString()
        val reportRequest : ReportPostRequest = ReportPostRequest(postIdx , getUserIdx() , text)
        Log.d("report" , "${reportRequest.toString()}")
        val reportService = ReportService()
        reportService.setReportPostView(this)
        reportService.reportPost(reportRequest)
    }

    //신고 성공
    override fun getReportPostSuccess(response: ReportPostResponse) {
        showToast(response.message)
        Log.d("삭제" , "삭제 성공")
        this.dismiss()
    }


    //신고 실패
    override fun getReportPostFailure(message: String) {
        showToast(message)
    }


}