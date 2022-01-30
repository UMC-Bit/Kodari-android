package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateCoinPostResult
import com.bit.kodari.databinding.ListItemMyWritingBinding

class DebateCoinPostRVAdapter(var coinPostList:ArrayList<DebateCoinPostResult>) : RecyclerView.Adapter<DebateCoinPostRVAdapter.MyViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item:DebateCoinPostResult )
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class MyViewHolder(val binding: ListItemMyWritingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : DebateCoinPostResult) {
            binding.listItemMyWritingCoinNameTv.text = item.symbol
            binding.listItemMyWritingNicknameTv.text = item.nickName
            binding.listItemMyWritingCommentCountTv.text = item.comment_cnt.toString()
            binding.listItemMyWritingLikeCnt.text = item.like.toString()
            binding.listItemMyWritingDislikeCnt.text = item.dislike.toString()
            //binding.listItemMyWritingImageIv.setImageBitmap(item.profileImgUrl)
            //Url로 프로필 그리기

        }
    }

    override fun getItemCount(): Int = coinPostList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemMyWritingBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(coinPostList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(coinPostList[position]) }
    }
}