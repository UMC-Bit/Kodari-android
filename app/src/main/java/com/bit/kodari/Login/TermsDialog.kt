package com.bit.kodari.Login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogTermsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class TermsDialog : DialogFragment() {
    lateinit var binding : DialogTermsBinding
    var check = false
    lateinit var size : Point
    lateinit var termsView: TermsView

    override fun onResume() {
        super.onResume()
        //여백 비율로 DialogFragment 크기 조절
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceHeight* 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
//
//        dialog!!.window!!.setLayout(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT)
    }
    fun setTermsInterface(termsView: TermsView){
        this.termsView = termsView
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = DialogTermsBinding.inflate(inflater , container, false)
        getDeviceSize()
        binding.termsSuccessBtn.setOnClickListener {
            termsView.setAgree(true)
            dismiss()
        }
        binding.termsExit.setOnClickListener {
            dismiss()
        }
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return binding.root
    }
    //디바이스 크기 구하기
    fun getDeviceSize(){
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        size = Point()
        display.getSize(size)

    }

}