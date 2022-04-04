package com.bit.kodari.Debate.Report

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.os.bundleOf
import com.bit.kodari.Config.BaseDialogFragment
import com.bit.kodari.Debate.Report.ReportData.ReportUserReqeust
import com.bit.kodari.Debate.Report.ReportData.ReportUserResponse
import com.bit.kodari.Debate.Report.ReportService.ReportService
import com.bit.kodari.Debate.Report.Retrofit.ReportUserView
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.DialogUserBlockConfirmBinding

class UserBlockConfirmDialog(val idx:Int) : BaseDialogFragment<DialogUserBlockConfirmBinding>(DialogUserBlockConfirmBinding::inflate) , ReportUserView {
    override fun initAfterBinding() {
        setString()
        setListener()
    }

    //글자 색 부분적으로 빨갛게 만들기
    private fun setString() {
        val builder = SpannableStringBuilder(binding.userBlockMessageTv.text)
        builder.setSpan(ForegroundColorSpan(Color.RED) , 7,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.userBlockMessageTv.setText(builder)
    }

    private fun setListener() {
        binding.userBlockDialogDeleteBtn.setOnClickListener {
            //api 호출
            callReportUser()
        }
        binding.userBlockDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

    fun callReportUser(){
        //유저 id , postIdx 이용해서 API 호출
        val reportUserRequest = ReportUserReqeust(idx, getUserIdx())        //postIdx와 내 userIdx 설정
        val reportService = ReportService()
        reportService.setReportUserView(this)
        reportService.reportUser(reportUserRequest)
    }

    override fun getReportUserSuccess(response: ReportUserResponse) {
        //유저 차단 성공
        showToast("유저 차단에 성공하였습니다.")
        Log.d("신고" , "유자 신고 성공")
        val bundle = bundleOf("success2" to true)        //신고 완료되었다고 보내기
        requireActivity().supportFragmentManager.setFragmentResult("request2",bundle)
        dismiss()

    }

    override fun getReportUserFailure(message: String) {
        //유저 차단 실패
        showToast(message)
    }
}