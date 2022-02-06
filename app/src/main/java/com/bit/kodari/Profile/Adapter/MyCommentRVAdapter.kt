package com.bit.kodari.Profile.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Profile.RetrofitData.GetMyCommentResult
import com.bit.kodari.databinding.ListItemMyCommentBinding

class MyCommentRVAdapter(var postList: ArrayList<GetMyCommentResult>) : RecyclerView.Adapter<MyCommentRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ListItemMyCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindComment(item : GetMyCommentResult) {
            binding.listItemMyCommentTv.text = item.content
            binding.listItemMyCommentTimeTv.text = item.time
            binding.listItemMyCommentCoinNameTv.text = item.postList[0].symbol
            binding.listItemMyCommentNicknameTv.text = item.postList[0].nickName
            binding.listItemMyCommentCommentCountNumTv.text = item.postList[0].comment_cnt.toString()
            binding.listItemMyCommentUpCntTv.text = item.postList[0].like.toString()
            binding.listItemMyCommentDownCntTv.text = item.postList[0].dislike.toString()
            binding.listItemMyCommentContentTv.text = item.postList[0].content
        }

    }
    override fun getItemCount(): Int = postList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemMyCommentBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindComment(postList[position])
    }

}