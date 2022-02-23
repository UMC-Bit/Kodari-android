package com.bit.kodari.Main.Account

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
import com.bit.kodari.Debate.Retrofit.DebateDeletePostView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Main.RetrofitInterface.DeleteTradeView
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.PossessionCoin.RetrofitData.DeleteTradeResponse
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogDeleteBinding
//글자만 거래내역을 삭제하시겠습니까?로 변경
class DeleteTradeDialog: DialogFragment() ,DeleteTradeView{
    private var _binding : DialogDeleteBinding? = null
    private val binding get() = _binding!!
    private var tradeIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDeleteBinding.inflate(inflater,container,false)
        if(requireArguments().containsKey("tradeIdx")){
            tradeIdx = requireArguments().getInt("tradeIdx")
        }

        //"삭제" 부분 빨갛게 만들기
        val builder = SpannableStringBuilder("거래내역을 삭제하시겠습니까?")
        builder.setSpan(ForegroundColorSpan(Color.RED) , 6,8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.deleteMessageTv.setText(builder)

        setListener()
        return binding.root
    }

    fun setListener() {
        binding.deleteDialogDeleteBtn.setOnClickListener {
            //거래내역 삭제 API 호출
            callDeleteTrade(tradeIdx)

        }
        binding.deleteDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun deleteTradeSuccess(response: DeleteTradeResponse) {
        when(response.code){
            1000 -> {
                Toast.makeText(context,"${response.result}",Toast.LENGTH_SHORT).show()
                dismiss()
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_container_fl , DebateMainFragment()).commitAllowingStateLoss()

            }
            else -> {
                Toast.makeText(context, "${response.message}" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun deleteTradeFailure(message: String) {
        Log.d("deletPost" ,"삭제 실패")
    }

    private fun callDeleteTrade(tradeIdx:Int){
        val homeService = HomeService()
        homeService.setDeleteTradeView(this)
        homeService.deleteTrade(tradeIdx)
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