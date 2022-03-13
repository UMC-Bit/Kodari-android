package com.bit.kodari.Debate

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.PostData.DebateModifyRequest
import com.bit.kodari.Debate.PostData.DebateModifyResponse
import com.bit.kodari.Debate.Retrofit.DebateModifyPostView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.Util.getJwt
import com.bit.kodari.databinding.FragmentDebateModifyPostBinding
import com.bumptech.glide.Glide
import kotlin.properties.Delegates


class DebateModifyPostFragment(var coinName :String , var coinIdx: Int) : BaseFragment<FragmentDebateModifyPostBinding>(FragmentDebateModifyPostBinding::inflate) , DebateModifyPostView {

    private var postIdx = 0
    private var flag by Delegates.notNull<Int>()
    private lateinit var callback:OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val tempPostIdx = postIdx
                val tempFlag = flag
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , DebateMineFragment(tempFlag,coinName).apply {
                        arguments = Bundle().apply {
                            putInt("postIdx", tempPostIdx)
                        }
                    }).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        getPostInfo()                //게시글 Index가져오기
        setListener()
        Log.d("modifyPost" , "postIdx : ${postIdx}")
    }

    //작성되어 있는 게시글 정보 가져오기
    fun getPostInfo(){
        if(requireArguments().containsKey("postIdx")){
            Log.d("postIdx" , "실행 ")
            postIdx = requireArguments().getInt("postIdx")
            Log.d("postIdx" , "받아온 postIdx : ${postIdx}")
        }
        if(requireArguments().containsKey("imgUrl")){
            Log.d("imgUrl","${requireArguments().getString("imgUrl")}")
            Glide.with(binding.modifyProfileIv)
                .load(requireArguments().getString("imgUrl"))
                .error(R.drawable.profile_image)
                .into(binding.modifyProfileIv)
        }
        if(requireArguments().containsKey("nickName")){
            binding.modifyNicknameTv.text = requireArguments().getString("nickName")
        }
        if(requireArguments().containsKey("content")){
            //EditText는 setText해야한다.
            binding.modifyContentEt.setText(requireArguments().getString("content"))
        }

        if(requireArguments().containsKey("flag")){
            flag = requireArguments().getInt("flag")
        }
    }

    fun setListener(){
        binding.modifyCompleteBtnTv.setOnClickListener {
            val debateModifyRequest = DebateModifyRequest(binding.modifyContentEt.text.toString())
            val debateService = DebateService()
            debateService.setDebateModifyPostView(this)
            showLoadingDialog(requireContext())
            debateService.modifyPost(postIdx , debateModifyRequest)
        }

        binding.modifyBackBtn.setOnClickListener {
            val tempPostIdx = postIdx
            val tempFlag = flag
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , DebateMineFragment(tempFlag,coinName).apply {
                    arguments = Bundle().apply {
                        putInt("postIdx", tempPostIdx)
                    }
                }).commit()
        }


    }

    override fun updatePostSuccess(response: DebateModifyResponse) {
        Log.d("updatePost", "${getJwt()}")
        Log.d("updatePost","${response}")
        when(response.code){
          1000 -> {
              showToast("${response.result}")
              requireActivity().supportFragmentManager.beginTransaction()
                  .replace(R.id.main_container_fl , DebateMineFragment(2,coinName).apply {
                      arguments = Bundle().apply {
                          putInt("postIdx" , postIdx)
                          putInt("coinIdx" , coinIdx)
                      }
                  }).commitAllowingStateLoss()
          }
          else -> {
              Log.d("updatePost" , "${response.code}")
              showToast("${response.message}")
          }
        }
        dismissLoadingDialog()
    }

    override fun updatePostFailure(message: String) {
        Log.d("updatePost" , "통신 실패.")
        dismissLoadingDialog()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}