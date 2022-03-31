package com.bit.kodari.Profile.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.PostData.DebatePostResult
import com.bit.kodari.Profile.RetrofitData.GetMyCommentResult
import com.bit.kodari.R
import com.bit.kodari.databinding.ListItemMyCommentBinding
import com.bumptech.glide.Glide

class MyCommentRVAdapter(var postList: ArrayList<GetMyCommentResult>) : RecyclerView.Adapter<MyCommentRVAdapter.MyViewHolder>() {

    //클릭했을때 해당 게시글로 가게하기 위해서.
    interface MyItemClickListener {
        fun onItemClick(item: GetMyCommentResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

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
            Glide.with(binding.listItemMyCommentImageIv)
                .load(item.postList[0].profileImgUrl)
                .error(R.drawable.ic_basic_profile)
                .placeholder(R.drawable.ic_basic_profile)
                .into(binding.listItemMyCommentImageIv)
        }

    }
    override fun getItemCount(): Int = postList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemMyCommentBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindComment(postList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick(postList[position])}
    }

}