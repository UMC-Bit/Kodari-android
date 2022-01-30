package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateCoinResult
import com.bit.kodari.Debate.Data.DebatePostResponse
import com.bit.kodari.Debate.Data.DebatePostResult
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bit.kodari.databinding.ListItemMyWritingBinding

class DebateMainRVAdapter(var postList:ArrayList<DebatePostResult>) : RecyclerView.Adapter<DebateMainRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ListItemMyWritingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : DebatePostResult) {
            binding.listItemMyWritingCoinNameTv.text = item.symbol
            binding.listItemMyWritingNicknameTv.text = item.nickName
            binding.listItemMyWritingCommentCountTv.text = item.comment_cnt.toString()
            binding.listItemMyWritingLikeCnt.text = item.like.toString()
            binding.listItemMyWritingDislikeCnt.text = item.dislike.toString()
            //binding.listItemMyWritingImageIv.setImageBitmap(item.profileImgUrl)
            //Url로 프로필 그리기

        }
    }

    override fun getItemCount(): Int = postList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemMyWritingBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(postList[position])
    }
}