package com.bit.kodari.Main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogModifyInfoBinding
import kotlin.properties.Delegates


//커스텀 다이얼로그 만들기
class ModifyInfoDialog() : DialogFragment() {

    private var _binding : DialogModifyInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountName : String
    private var money by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogModifyInfoBinding.inflate(inflater, container, false)
        setListener()
        return binding.root
    }

    fun setListener() {

        binding.infoDialogExitBtn.setOnClickListener {
            dismiss()
        }

        binding.infoDialogModifyNameBtn.setOnClickListener {
            accountName = binding.infoDialogInputAccountEt.text.toString()
        }

        binding.infoDialogModifyCashBtn.setOnClickListener {
            money = binding.infoDialogInputCashEt.text.toString().toInt()
        }

    }

    //다이얼로그 크기는 onResume에서 window이용해서 선언
    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
    }


    //메모리 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}