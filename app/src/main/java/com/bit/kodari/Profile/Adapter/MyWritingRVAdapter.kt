package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Profile.RetrofitData.GetMyPostResult
import com.bit.kodari.R
import com.bit.kodari.databinding.ListItemMyWritingBinding
import com.bumptech.glide.Glide

class MyWritingRVAdapter(var postList:ArrayList<GetMyPostResult>) : RecyclerView.Adapter<MyWritingRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ListItemMyWritingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : GetMyPostResult) {
            binding.listItemMyWritingCoinNameTv.text = item.symbol
            binding.listItemMyWritingNicknameTv.text = item.nickName
            binding.listItemMyWritingCommentCountTv.text = item.comment_cnt.toString()
            binding.listItemMyWritingLikeCnt.text = item.like.toString()
            binding.listItemMyWritingDislikeCnt.text = item.dislike.toString()
            binding.listItemMyWritingContentTv.text = item.content
            //binding.listItemMyWritingImageIv.setImageBitmap(item.profileImgUrl)
            //Url로 프로필 그리기
            Glide.with(binding.listItemMyWritingImageIv)
                .load(item.profileImgUrl)
                .error(R.drawable.ic_basic_profile)
                .placeholder(R.drawable.ic_basic_profile)
                .into(binding.listItemMyWritingImageIv)

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