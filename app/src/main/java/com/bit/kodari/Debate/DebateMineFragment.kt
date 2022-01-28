package com.bit.kodari.Debate

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.bit.kodari.databinding.FragmentDebateMineBinding


class DebateMineFragment : Fragment() {

    lateinit var binding:FragmentDebateMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDebateMineBinding.inflate(inflater , container , false)
        setListener()
        setInit()
        return binding.root
    }


    //초기 기본 셋팅 값들 설정
    fun setInit(){
        //인스타그램처럼 보내기 , 자동 개행 기능 부여
        binding.mineCommentInputMessageEt.imeOptions = EditorInfo.IME_ACTION_SEND
        binding.mineCommentInputMessageEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    //초기 리스너들 설정
    fun setListener(){
        //다른 곳 클릭시 올라온 키보드 내려가게함
        binding.debateMineRoot.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }
        binding.mineCommentInputMessageEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable?.length!! > 0) {
                    binding.mineCommentSendBtn.setClickable(true);
                    binding.mineCommentSendBtn.setBackgroundColor(Color.BLUE);
                } else {
                    binding.mineCommentSendBtn.setClickable(false);
                    binding.mineCommentSendBtn.setBackgroundColor(Color.GRAY);
                }
            }
        })
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
}