package com.bit.kodari.Debate.Service

import android.util.Log
import com.bit.kodari.Debate.CommentData.*
import com.bit.kodari.Debate.LikeData.CommentLikeRequest
import com.bit.kodari.Debate.LikeData.CommentLikeResponse
import com.bit.kodari.Debate.LikeData.PostLikeRequest
import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.*
import com.bit.kodari.Debate.Retrofit.*
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import com.bit.kodari.Util.getUserIdx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateService {

    private lateinit var debateCoinView: DebateCoinView
    private lateinit var debateMainView: DebateMainView             //좋아요 , 싫어요 기능 구현
    private lateinit var debateCoinPostView: DebateCoinPostView     //좋아요, 싫어요 기능 구현
    private lateinit var debatePostWriteVIew: DebatePostWriteVIew
    private lateinit var debateMineView: DebateMineView
    private lateinit var debateModifyPostView: DebateModifyPostView
    private lateinit var debateDeletePostView : DebateDeletePostView

    fun setDebateCoinView(debateCoinView:DebateCoinView){
        this.debateCoinView = debateCoinView
    }

    fun setDebateMainView(debateMainView: DebateMainView){
        this.debateMainView = debateMainView
    }

    fun setDebateCoinPostView(debateCoinPostView: DebateCoinPostView){
        this.debateCoinPostView = debateCoinPostView
    }

    fun setDebatePostWriteVIew(debatePostWriteVIew: DebatePostWriteVIew){
        this.debatePostWriteVIew = debatePostWriteVIew
    }

    fun setDebateMineView(debateMineView: DebateMineView){
        this.debateMineView = debateMineView
    }

    fun setDebateModifyPostView(debateModifyPostView:DebateModifyPostView){
        this.debateModifyPostView = debateModifyPostView
    }

    fun setDebateDeletePostView(debateDeletePostView: DebateDeletePostView){
        this.debateDeletePostView = debateDeletePostView
    }

    fun getCoinsAll(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)

        debateService.getCoinsAll(getJwt()!!).enqueue(object : Callback<DebateCoinResponse>{
            override fun onResponse(
                call: Call<DebateCoinResponse>,
                response: Response<DebateCoinResponse>
            ) {
                debateCoinView.getCoinsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateCoinResponse>, t: Throwable) {
                debateCoinView.getCoinsAllFailure("t")
            }
        })
    }

    fun getPostsAll(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getPostsAll(getJwt()!!).enqueue(object : Callback<DebatePostResponse>{
            override fun onResponse(
                call: Call<DebatePostResponse>,
                response: Response<DebatePostResponse>
            ) {
                Log.d("debate" , "호출 성공 : ${response.body()}")
                debateMainView.getPostsAllSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebatePostResponse>, t: Throwable) {
                Log.d("debate" , "호출 실패")
                debateMainView.getPostsAllFailure("${t}")
            }
        })
    }

    fun getCoinPost(coinName:String){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getCoinPost(getJwt()!!,coinName).enqueue(object :Callback<DebateCoinPostResponse>{
            override fun onResponse(
                call: Call<DebateCoinPostResponse>,
                response: Response<DebateCoinPostResponse>
            ) {
                debateCoinPostView.getCoinPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateCoinPostResponse>, t: Throwable) {
                debateCoinPostView.getCoinPostFailure("${t}")
            }
        })
    }

    fun writePost(post:DebateWritePostRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        Log.d("writePost" ,"${getJwt()}")
        debateService.writePost(getJwt()!!, post).enqueue(object : Callback<DebateWritePostResponse>{
            override fun onResponse(
                call: Call<DebateWritePostResponse>,
                response: Response<DebateWritePostResponse>
            ) {
                Log.d("post" , "${response.code()} ")
                debatePostWriteVIew.updatePostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateWritePostResponse>, t: Throwable) {
                debatePostWriteVIew.updatePostFailure("${t}")
            }
        })
    }

    fun selectPost(postIdx: Int){       //
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.selectPost(getJwt()!!,postIdx).enqueue(object : Callback<DebateSelectPostResponse>{
            override fun onResponse(
                call: Call<DebateSelectPostResponse>,
                response: Response<DebateSelectPostResponse>
            ) {
                Log.d("selectPost" , "코드 : ${response.body()!!.code}")
                Log.d("selectPost" , "코드 : ${response.body()!!}")
                debateMineView.selectPostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateSelectPostResponse>, t: Throwable) {
                debateMineView.selectPostFailure("${t}")
            }
        })
    }

    fun getUserInfo(){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.getUserInfo(getUserIdx()).enqueue(object : Callback<GetProfileResponse>{
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                debatePostWriteVIew.getUserInfoSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                debatePostWriteVIew.getUserInfoFailure("${t}")
            }
        })
    }

    fun modifyPost(postIdx:Int , debateModifyRequest: DebateModifyRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        Log.d("modifyPost" ,"${getJwt()}")
        debateService.updatePost(getJwt()!! , postIdx,debateModifyRequest).enqueue(object : Callback<DebateModifyResponse>{
            override fun onResponse(
                call: Call<DebateModifyResponse>,
                response: Response<DebateModifyResponse>
            ) {
                debateModifyPostView.updatePostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateModifyResponse>, t: Throwable) {
                debateModifyPostView.updatePostFailure("${t}")
            }
        })
    }

    fun deletePost(postIdx:Int){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        Log.d("deletePost" ,"${getJwt()}")
        debateService.deletePost(getJwt()!!,postIdx).enqueue(object : Callback<DebateDeletePostResponse>{
            override fun onResponse(
                call: Call<DebateDeletePostResponse>,
                response: Response<DebateDeletePostResponse>
            ) {
                debateDeletePostView.deletePostSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DebateDeletePostResponse>, t: Throwable) {
                debateDeletePostView.deletePostFailure("${t}")
            }
        })
    }

    //게시글 좋아요 누르기.
    fun postLike(postLikeRequest: PostLikeRequest, like:Int){   //like 가 0이면 좋아요 눌렀을 때 , 1이면 싫어요 눌렀을때
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.postLike(getJwt()!!, postLikeRequest).enqueue(object : Callback<PostLikeResponse>{
            override fun onResponse(
                call: Call<PostLikeResponse>,
                response: Response<PostLikeResponse>
            ) {
                debateMineView.postLikeSuccess(response.body()!! , like)
            }

            override fun onFailure(call: Call<PostLikeResponse>, t: Throwable) {
                debateMineView.postLikeFailure("${t}")
            }
        })
    }

    //댓글 등록 호출
    fun regComment(comment : RegCommentRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.regComment(getJwt()!!, comment).enqueue(object : Callback<RegCommentResponse>{
            override fun onResponse(
                call: Call<RegCommentResponse>,
                response: Response<RegCommentResponse>
            ) {
                debateMineView.regCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RegCommentResponse>, t: Throwable) {
                debateMineView.regCommentFailure("${t}")
            }
        })
    }

    //댓글 수정 호출
    fun modifyComment(postCommentIdx: Int , modifyCommentRequest: ModifyCommentRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.modifyComment(getJwt()!!, postCommentIdx , modifyCommentRequest).enqueue(object : Callback<ModifyCommentResponse>{
            override fun onResponse(
                call: Call<ModifyCommentResponse>,
                response: Response<ModifyCommentResponse>
            ) {
                debateMineView.modifyCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ModifyCommentResponse>, t: Throwable) {
                debateMineView.modifyCommentFailure("${t}")
            }
        })
    }

    //댓글 삭제 호출
    fun deleteComment(postCommentIdx: Int ){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.deleteComment(getJwt()!!, postCommentIdx ).enqueue(object : Callback<DeleteCommentResponse>{
            override fun onResponse(
                call: Call<DeleteCommentResponse>,
                response: Response<DeleteCommentResponse>
            ) {
                debateMineView.deleteCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {
                debateMineView.deleteCommentFailure("${t}")
            }
        })
    }

    //대댓 등록
    fun regReComment(regReCommentRequest: RegReCommentRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.regReComment(getJwt()!!, regReCommentRequest ).enqueue(object : Callback<RegReCommentResponse>{
            override fun onResponse(
                call: Call<RegReCommentResponse>,
                response: Response<RegReCommentResponse>
            ) {
                debateMineView.regReCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<RegReCommentResponse>, t: Throwable) {
                debateMineView.regReCommentFailure("${t}")
            }
        })
    }

    //대댓 삭제
    fun deleteReComment(postReplyIdx: Int){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        debateService.deleteReComment(getJwt()!! , postReplyIdx).enqueue(object : Callback<DeleteReCommentResponse>{
            override fun onResponse(
                call: Call<DeleteReCommentResponse>,
                response: Response<DeleteReCommentResponse>
            ) {
                Log.d("deletRe" , "${response.body()!!}")
                debateMineView.deleteReCommentSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<DeleteReCommentResponse>, t: Throwable) {
                debateMineView.deleteReCommentFailure("${t}")
            }
        })
    }

    //좋아요 버튼 눌렀을 때.
    fun pressCommentLike(commentLikeRequest: CommentLikeRequest){
        val debateService = getRetorfit().create(DebateRetrofitInterface::class.java)
        Log.d("like" , "보내는 값 : ${commentLikeRequest}")
        debateService.pressCommentLike(getJwt()!!, commentLikeRequest).enqueue(object : Callback<CommentLikeResponse>{
            override fun onResponse(
                call: Call<CommentLikeResponse>,
                response: Response<CommentLikeResponse>
            ) {
                Log.d("like", "jwt : ${getJwt()}")
                Log.d("like" , "${response.body()}")
                debateMineView.pressCommentLikeSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<CommentLikeResponse>, t: Throwable) {
                debateMineView.pressCommentLikeFailure("${t}")
            }
        })
    }

}