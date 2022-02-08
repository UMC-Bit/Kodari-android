package com.bit.kodari.Debate.Adapter

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
class DebateReCommentRVAdapter(var recommentList:ArrayList<DebateSelectPostReply>) : RecyclerView.Adapter<DebateReCommentRVAdapter.MyViewHolder>() {

    //클릭했을때 해당 게시글로 가게하기 위해서.
    interface MyItemClickListener {
        fun onDeleteClick(item: DebateSelectPostReply)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

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
            binding.listReCommentNicknameTv.text = item.nickName
            if(item.checkReplyWriter){          //내가 쓴글이면 삭제 버튼 활성화
                binding.listItemReDeleteTv.visibility = View.VISIBLE
            }else{                              //내가 쓴글이 아니면 삭제버튼 비활성화
                binding.listItemReDeleteTv.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListReCommentBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recommentList[position])
        holder.binding.listItemReDeleteTv.setOnClickListener { mItemClickListener.onDeleteClick(recommentList[position]) }
    }

    override fun getItemCount(): Int = recommentList.size
}