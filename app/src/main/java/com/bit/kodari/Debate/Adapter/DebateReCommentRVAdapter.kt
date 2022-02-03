package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateSelectPostReply
import com.bit.kodari.R
import com.bit.kodari.databinding.ListReCommentBinding
import com.bumptech.glide.Glide

//replyList를 할당받으면됨
class DebateReCommentRVAdapter(var recommentList:ArrayList<DebateSelectPostReply>) : RecyclerView.Adapter<DebateReCommentRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ListReCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DebateSelectPostReply){
            binding.listItemReContentTv.text = item.content
            //binding.listReCommentMaskIv.setImageBitmap(~)     url로 이미지 가져오기
            if(item.profileImgUrl != ""){
                Glide.with(binding.listReCommentMaskIv)
                    .load(item.profileImgUrl)
                    .error(R.drawable.profile_image)
                    .into(binding.listReCommentMaskIv)
            }
            binding.listItemReTimeTv.text = item.time
            binding.listReCommentNicknameTv.text = item.nickName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListReCommentBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recommentList[position])
    }

    override fun getItemCount(): Int = recommentList.size
}