package com.bit.kodari.Debate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Data.DebateSelectPostResponse
import com.bit.kodari.Debate.Retrofit.DebateModifyPostView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentDebateModifyPostBinding
import com.bumptech.glide.Glide


class DebateModifyPostFragment : BaseFragment<FragmentDebateModifyPostBinding>(FragmentDebateModifyPostBinding::inflate) , DebateModifyPostView {

    var postIdx = 0

    override fun initAfterBinding() {
        getPostInfo()                //게시글 Index가져오기
    }

    //작성되어 있는 게시글 정보 가져오기
    fun getPostInfo(){
        if(requireArguments().containsKey("postIdx")){
            postIdx = requireArguments().getInt("postIdx")
        }
        if(requireArguments().containsKey("imgUrl")){
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
    }

    override fun updatePostSuccess() {

    }

    override fun updatePostFailure() {

    }
}