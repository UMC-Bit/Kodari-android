package com.bit.kodari.Debate.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateCoinPostResult
import com.bit.kodari.Debate.Data.DebateSelectPostComment
import com.bit.kodari.R
import com.bit.kodari.databinding.ListCommentBinding
import com.bumptech.glide.Glide


//기본적으로 SelectPostComment 와 list_comment 이용
//그 뒤 또 다른 RecyclerViewAdapter를 만들어야 한다 .
//CommentList 를 넘겨받으면됌
class DebateCommentRVAdapter(var commentList : ArrayList<DebateSelectPostComment>) : RecyclerView.Adapter<DebateCommentRVAdapter.MyViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(binding: ListCommentBinding)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class MyViewHolder(val binding:ListCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DebateSelectPostComment){

            binding.listCommentNicknameTv.text = item.nickName
            binding.listItemContentTv.text = item.content
            binding.listItemCommentLikeCntTv.text = "좋아요 ${item.like}개"
            binding.listItemTimeTv.text = item.time.toString()
            if(item.profileImgUrl != ""){
                Glide.with(binding.listCommentMaskIv)
                    .load(item.profileImgUrl)
                    .error(R.drawable.profile_image)
                    .into(binding.listCommentMaskIv)
            }
            if(item.replyList.size == 0){                       //대댓 없으면
                binding.listItemCommentAskTv.visibility = View.VISIBLE      //대댓 없으면 답글 달기 보이게 함
                binding.listItemCommentAskTv.text = "답글 달기"               //눌렀을때 답글 달게해야함 .
                binding.listItemNowCommentTv.visibility = View.GONE
            } else{     //대댓이 존재하면 답글 5개 형식으로 보이게함 -> 얘네들 클릭리스너도 구현해야함 ..할수있을지도 ?
                binding.listItemCommentAskTv.visibility = View.GONE
                binding.listItemNowCommentTv.visibility =View.VISIBLE
                binding.listItemNowCommentTv.text = "답글 ${item.replyList.size}개"
            }

//            //답글 달기 눌렀을 대 대댓창 보이게하기.
//            binding.listItemNowCommentTv.setOnClickListener {
//                if(rvFlage){                    //가려져 있으면 클릭했을때 보여야함
//                    binding.listItemReCommentRv.visibility = View.VISIBLE
//                    rvFlage = true
//                    binding.listItemNowCommentTv.text = "답글 숨기기"
//                } else{                         //대댓글들이 보일때
//                    binding.listItemReCommentRv.visibility = View.GONE
//                    rvFlage = false
//                    binding.listItemNowCommentTv.text = "답글 ${item.replyList.size}개"
//                }
//            }

            //대댓 어댑터도 사용해야함.
            val debateReCommentRVAdapter = DebateReCommentRVAdapter(item.replyList)
            //context로 최상단 레이아웃의 context 전달
            binding.listItemReCommentRv.layoutManager = LinearLayoutManager(binding.commentRoot.context,LinearLayoutManager.VERTICAL , false)
            binding.listItemReCommentRv.adapter = debateReCommentRVAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListCommentBinding.inflate(LayoutInflater.from(parent.context) , parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(commentList[position])
        var rvFlage = false         //false면 안보임 ->기본값
//        holder.binding.listItemCommentAskTv.setOnClickListener {
//            //Log.d("test" , "클릭 리스너 테스트 ")
//            if(rvFlage){                    //가려져 있으면 클릭했을때 보여야함
//                //Log.d("test" , "리사이클러뷰 보여 ")
//                holder.binding.listItemReCommentRv.visibility = View.VISIBLE
//                rvFlage = true
//                holder.binding.listItemNowCommentTv.text = "답글 ${commentList[position].replyList.size}개"
//            } else{                         //대댓글들이 보일때
//                //Log.d("test" , "리사이클러뷰 안보여")
//                holder.binding.listItemReCommentRv.visibility = View.GONE
//                rvFlage = false
//                holder.binding.listItemNowCommentTv.text = "답글 숨기기"
//            }
//        }

        holder.binding.listItemNowCommentTv.setOnClickListener {
            Log.d("test" , "클릭 리스너 테스트 ")
            if(rvFlage){                    //가려져 있으면 클릭했을때 보여야함
                Log.d("test" , "리사이클러뷰 안보여 ")
                holder.binding.listItemReCommentRv.visibility = View.GONE
                rvFlage = false
                holder.binding.listItemNowCommentTv.text = "답글 ${commentList[position].replyList.size}개"
            } else{                         //대댓글들이 보일때
                Log.d("test" , "리사이클러뷰 보여")
                holder.binding.listItemReCommentRv.visibility = View.VISIBLE
                rvFlage = true
                holder.binding.listItemNowCommentTv.text = "답글 숨기기"
            }
        }
    }


    override fun getItemCount(): Int = commentList.size
}