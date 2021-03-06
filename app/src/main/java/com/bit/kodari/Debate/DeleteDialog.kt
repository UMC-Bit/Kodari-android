package com.bit.kodari.Debate

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bit.kodari.Debate.PostData.DebateDeletePostResponse
import com.bit.kodari.Debate.Retrofit.DebateDeletePostView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogDeleteBinding

class DeleteDialog: DialogFragment() , DebateDeletePostView{
    private var _binding : DialogDeleteBinding? = null
    private val binding get() = _binding!!
    var postIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDeleteBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        if(requireArguments().containsKey("postIdx")){
            postIdx = requireArguments().getInt("postIdx")
        }

        //"삭제" 부분 빨갛게 만들기
        val builder = SpannableStringBuilder(binding.deleteMessageTv.text)
        builder.setSpan(ForegroundColorSpan(Color.RED) , 5,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.deleteMessageTv.setText(builder)

        setListener()
        return binding.root
    }

    fun setListener() {
        binding.deleteDialogDeleteBtn.setOnClickListener {
            //게시글 삭제 API 호출 ->
            val debateService = DebateService()
            debateService.setDebateDeletePostView(this)
            debateService.deletePost(postIdx)
        }
        binding.deleteDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun deletePostSuccess(response: DebateDeletePostResponse) {
        when(response.code){
          1000 -> {
              Toast.makeText(context,"${response.result}",Toast.LENGTH_SHORT).show()
              dismiss()
              requireActivity().supportFragmentManager.beginTransaction()
                  .replace(R.id.main_container_fl , DebateMainFragment()).commitAllowingStateLoss()

          }
          else -> {
              Toast.makeText(context, "${response.message}" , Toast.LENGTH_SHORT).show()
          }
        }
    }

    override fun deletePostFailure(message: String) {
        Log.d("deletPost" ,"삭제 실패")
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