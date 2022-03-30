package com.bit.kodari.Debate.Report

import android.util.Log
import androidx.core.os.bundleOf
import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.Debate.Report.ReportData.ReportCommentRequest
import com.bit.kodari.Debate.Report.ReportData.ReportPostRequest
import com.bit.kodari.Debate.Report.ReportData.ReportRecommentRequest
import com.bit.kodari.Debate.Report.ReportData.ReportResponse
import com.bit.kodari.Debate.Report.ReportService.ReportService
import com.bit.kodari.Debate.Report.Retrofit.ReportPostView
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.DialogReportReasonBinding

//Sell로 넘기는데  인덱스 다르게 해서 처리해보자

class ReportReasonDialog(val Idx: Int, val index: Int ,val flag: Int) :
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
                callReportPost()
            } else if(flag == 2){       //댓글 신고 기능
                callReportComment()
            }else{                      //대댓 신고 기능
                callReportRecomment()
            }

        }

        binding.dialogReportReasonSellCancelTV.setOnClickListener {
            dismiss()
        }
    }

    private fun callReportPost() {
        val text = binding.dialogReportReasonSellTitleTV.text.toString()
        val reportRequest : ReportPostRequest = ReportPostRequest(Idx , getUserIdx() , text)
        Log.d("report" , "${reportRequest.toString()}")
        val reportService = ReportService()
        reportService.setReportPostView(this)
        reportService.reportPost(reportRequest)
    }

    private fun callReportComment() {
        val text = binding.dialogReportReasonSellTitleTV.text.toString()
        val reportRequest : ReportCommentRequest = ReportCommentRequest(Idx , getUserIdx() , text)
        Log.d("report" , "${reportRequest.toString()}")
        val reportService = ReportService()
        reportService.setReportPostView(this)
        reportService.reportComment(reportRequest)
    }

    private fun callReportRecomment() {
        val text = binding.dialogReportReasonSellTitleTV.text.toString()
        val reportRequest : ReportRecommentRequest = ReportRecommentRequest(Idx , getUserIdx() , text)
        Log.d("report" , "${reportRequest.toString()}")
        val reportService = ReportService()
        reportService.setReportPostView(this)
        reportService.reportRecomment(reportRequest)
    }

    //신고 성공
    override fun getReportPostSuccess(response: ReportResponse) {
        showToast(response.message)
        Log.d("신고" , "게시글 신고 성공")
        Log.d("신고 닫기3" ,"신고 닫기 3")
        val bundle = bundleOf("success" to true)        //신고 완료되었다고 보내기
        requireActivity().supportFragmentManager.setFragmentResult("request",bundle)
        dismiss()
    }


    //신고 실패
    override fun getReportPostFailure(message: String) {
        showToast(message)
    }

    //댓글 신고 성공
    override fun getReportCommentSuccess(response: ReportResponse) {
        showToast(response.message)
        Log.d("신고" , "댓글 신고 성공")
        val bundle = bundleOf("success" to true)
        requireActivity().supportFragmentManager.setFragmentResult("request",bundle)
        dismiss()
    }

    //댓글 신고 실패
    override fun getReportCommentFailure(message: String) {
        showToast(message)
    }


    //대댓 신고 성공
    override fun getReportRecommentSuccess(response: ReportResponse) {
        showToast(response.message)
        Log.d("신고" , "대댓 신고 성공")
        val bundle = bundleOf("success" to true)
        requireActivity().supportFragmentManager.setFragmentResult("request",bundle)
        dismiss()
    }
    //대댓 신고 실패
    override fun getReportRecommentFailure(message: String) {
        showToast(message)
    }


}