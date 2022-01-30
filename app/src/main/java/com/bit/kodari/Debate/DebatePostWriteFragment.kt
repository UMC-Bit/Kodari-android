package com.bit.kodari.Debate

import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Data.DebateUpdatePostRequest
import com.bit.kodari.Debate.Data.DebateUpdatePostResponse
import com.bit.kodari.Debate.Data.DebateUpdatePostResult
import com.bit.kodari.Debate.Retrofit.DebatePostWriteVIew
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentDebatePostWriteBinding

//글 입력할때 프로필이랑 뜨게해야함

class DebatePostWriteFragment : BaseFragment<FragmentDebatePostWriteBinding>(FragmentDebatePostWriteBinding::inflate) ,DebatePostWriteVIew {
    override fun initAfterBinding() {
        if(arguments?.isEmpty != null){
            var coinName = requireArguments().getString("coinName")!!
            binding.postWriteSymbolTv.text = coinName
        }
        setInit()
        setListener()
    }

    //초기 기본 셋팅 값들 설정
    fun setInit(){
        //인스타그램처럼 보내기 , 자동 개행 기능 부여
        binding.postWriteContentEt.imeOptions = EditorInfo.IME_ACTION_SEND
        binding.postWriteContentEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }


    fun setListener(){
        //다른 곳 클릭시 올라온 키보드 내려가게함
        binding.debatePostWriteRoot.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }

        binding.postWriteCompleteBtnTv.setOnClickListener {
            //게시글 올리기 API 활성화
            var post = DebateUpdatePostRequest(2,5,binding.postWriteContentEt.text.toString())
            val debateService =DebateService()
            debateService.setdebatePostWriteVIew(this)
            showLoadingDialog(requireContext())
            debateService.updatePost(post)

       }
    }

    //다른 곳 클릭시 올라온 키보드 내려가게함
    fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    //글쓰기 API 정상적으로 호출됐을때.
    override fun updatePostSuccess(response: DebateUpdatePostResponse) {
        dismissLoadingDialog()
        when(response.code){
          1000 -> {
              showToast(response.message)
          }
          else -> {
              showToast(response.message)
          }
        }
    }

    //글쓰기 API 정상적으로 아닐떄떄
    override fun updatePostFailure(message: String) {
        dismissLoadingDialog()
        showToast("통신실패")
    }
}