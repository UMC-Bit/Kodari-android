package com.bit.kodari.Profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bit.kodari.Debate.DebateMainFragment
import com.bit.kodari.Debate.PostData.DebateDeletePostResponse
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.R
import com.bit.kodari.Util.saveAutoLogin
import com.bit.kodari.Util.saveLoginInfo
import com.bit.kodari.databinding.DialogLogoutBinding

class LogOutDialog: DialogFragment() {
    private var _binding : DialogLogoutBinding? = null
    private val binding get() = _binding!!
    var postIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLogoutBinding.inflate(inflater,container,false)

        //"로그아웃" 부분 빨갛게 만들기
        val builder = SpannableStringBuilder(binding.logoutMessageTv.text)
        builder.setSpan(ForegroundColorSpan(Color.RED) , 0,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.logoutMessageTv.setText(builder)

        setListener()
        return binding.root
    }

    fun setListener() {
        binding.logoutDialogYesBtn.setOnClickListener {
            // 로그아웃
            saveLoginInfo(null, null, null, 0)     //0이면 유저 없는거
            saveAutoLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.logoutDialogNoBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
    }


    //메모리 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}