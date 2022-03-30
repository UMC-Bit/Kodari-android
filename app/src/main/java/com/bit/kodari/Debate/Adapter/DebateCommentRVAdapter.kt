package com.bit.kodari.Debate.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.PostData.DebateSelectPostComment
import com.bit.kodari.Debate.PostData.DebateSelectPostReply
import com.bit.kodari.R
import com.bit.kodari.databinding.ListCommentBinding
import com.bumptech.glide.Glide


//기본적으로 SelectPostComment 와 list_comment 이용
//그 뒤 또 다른 RecyclerViewAdapter를 만들어야 한다 .
//CommentList 를 넘겨받으면됌
class DebateCommentRVAdapter(var commentList : ArrayList<DebateSelectPostComment>) : RecyclerView.Adapter<DebateCommentRVAdapter.MyViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(binding: ListCommentBinding)
        fun onModifyClick(item:DebateSelectPostComment)
        fun onDeleteClick(item:DebateSelectPostComment)
        fun onRegReComment(item:DebateSelectPostComment)
        fun onLikeClick(item:DebateSelectPostComment)
        fun onClickReComment(postReplyIdx: Int)
        fun onMoreClick(item:DebateSelectPostComment)
        fun onReMoreClick(postReplyIdx: Int)
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
            if(item.profileImgUrl != ""){
                Glide.with(binding.listCommentMaskIv)
                    .load(item.profileImgUrl)
                    .error(R.drawable.ic_basic_profile)
                    .placeholder(R.drawable.ic_basic_profile)
                    .into(binding.listCommentMaskIv)
            }

            if(item.replyList.size == 0){                       //대댓 없으면
                binding.listItemNowCommentTv.visibility = View.GONE
                binding.commentBar.visibility = View.GONE
            } else{     //대댓이 존재하면 답글 5개 형식으로 보이게함 -> 얘네들 클릭리스너도 구현해야함 ..할수있을지도 ?
                binding.listItemNowCommentTv.text = "답글 ${item.replyList.size}개"
            }

            //내가 쓴 댓글이면 수정 , 삭제 보이게 .
            if(item.checkCommentWriter){
                binding.listItemModifyTv.visibility = View.VISIBLE
                binding.listItemDeleteTv.visibility = View.VISIBLE
                binding.listItemViewMoreTv.visibility = View.GONE
            } else{
                binding.listItemModifyTv.visibility = View.GONE
                binding.listItemDeleteTv.visibility = View.GONE
                binding.listItemViewMoreTv.visibility = View.VISIBLE
            }

            //내가 좋아요 눌렀던 댓글이면 하트 채워지게
            if(item.checkCommentLike){          //좋아요 누른글이면
                binding.listItemLikeOnIv.visibility = View.VISIBLE
                binding.listItemLikeOffIv.visibility = View.GONE
            } else{
                binding.listItemLikeOnIv.visibility = View.GONE
                binding.listItemLikeOffIv.visibility = View.VISIBLE
            }

            //삭제된 상태면 글자 색 그레이로.
            if(item.comment_status == "inactive"){
                binding.listItemContentTv.setTextColor(Color.GRAY)
            }

            //대댓 어댑터도 사용해야함.
            val debateReCommentRVAdapter = DebateReCommentRVAdapter(item.replyList , mItemClickListener)
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

        //채워진 하트 누르면 하트 없애주기
        holder.binding.listItemLikeOnIv.setOnClickListener {
            holder.binding.listItemLikeOnIv.visibility = View.GONE
            holder.binding.listItemLikeOffIv.visibility = View.VISIBLE
            var tempCnt = commentList[position].like
            Log.d("likeCnt" ,"${tempCnt}")
            mItemClickListener.onLikeClick(commentList[position])
            if(commentList[position].checkCommentLike){     //체크되어있으면
                holder.binding.listItemCommentLikeCntTv.text = "좋아요 ${tempCnt-1}개"
            }else{
                holder.binding.listItemCommentLikeCntTv.text = "좋아요 ${tempCnt}개"
            }

        }

        //빈 하트 누르면 하트 채우기.
        holder.binding.listItemLikeOffIv.setOnClickListener {
            holder.binding.listItemLikeOnIv.visibility = View.VISIBLE
            holder.binding.listItemLikeOffIv.visibility = View.GONE
            var tempCnt = commentList[position].like
            Log.d("likeCnt" ,"${tempCnt}")
            mItemClickListener.onLikeClick(commentList[position])
            if(commentList[position].checkCommentLike){
                holder.binding.listItemCommentLikeCntTv.text = "좋아요 ${tempCnt}개"
            }else{
                holder.binding.listItemCommentLikeCntTv.text = "좋아요 ${tempCnt+1}개"
            }


        }

        holder.binding.listItemModifyTv.setOnClickListener { mItemClickListener.onModifyClick(commentList[position])}
        holder.binding.listItemDeleteTv.setOnClickListener { mItemClickListener.onDeleteClick(commentList[position])}
        holder.binding.listItemCommentAskTv.setOnClickListener { mItemClickListener.onRegReComment(commentList[position]) }
        holder.binding.listItemViewMoreTv.setOnClickListener { mItemClickListener.onMoreClick(commentList[position]) }
    }


    override fun getItemCount(): Int = commentList.size

}