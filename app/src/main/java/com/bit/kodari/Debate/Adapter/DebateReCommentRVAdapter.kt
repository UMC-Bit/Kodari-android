package com.bit.kodari.Debate.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.PostData.DebatePostResult
import com.bit.kodari.Debate.PostData.DebateSelectPostReply
import com.bit.kodari.R
import com.bit.kodari.databinding.ListReCommentBinding
import com.bumptech.glide.Glide

//replyList를 할당받으면됨
class DebateReCommentRVAdapter(var recommentList:ArrayList<DebateSelectPostReply> , var mItemClickListener: DebateCommentRVAdapter.MyItemClickListener) : RecyclerView.Adapter<DebateReCommentRVAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: ListReCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DebateSelectPostReply){
            binding.listItemReContentTv.text = item.content
            binding.listRecommentTimeTv.text = item.time
            if(item.profileImgUrl != ""){
                Glide.with(binding.listReCommentMaskIv)
                    .load(item.profileImgUrl)
                    .error(R.drawable.ic_basic_profile)
                    .placeholder(R.drawable.ic_basic_profile)
                    .into(binding.listReCommentMaskIv)
            }
            binding.listReCommentNicknameTv.text = item.nickName
            if(item.checkReplyWriter){          //내가 쓴글이면 삭제 버튼 활성화
                binding.listItemReDeleteTv.visibility = View.VISIBLE
                binding.listItemReViewMoreTv.visibility = View.GONE
            }else{                              //내가 쓴글이 아니면 삭제버튼 비활성화
                binding.listItemReDeleteTv.visibility = View.GONE
                binding.listItemReViewMoreTv.visibility=View.VISIBLE
            }

            if(item.reply_status == "inactive"){
                binding.listItemReContentTv.setTextColor(Color.GRAY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListReCommentBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recommentList[position])
        //인터페이스가 구현된 곳에서 처리됩니다.
        holder.binding.listItemReDeleteTv.setOnClickListener { mItemClickListener.onClickReComment(recommentList[position].postReplyIdx) }
        holder.binding.listItemReViewMoreTv.setOnClickListener { mItemClickListener.onReMoreClick(recommentList[position].postReplyIdx) }
    }

    override fun getItemCount(): Int = recommentList.size
}