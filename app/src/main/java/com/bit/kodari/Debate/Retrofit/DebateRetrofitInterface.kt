package com.bit.kodari.Debate.Retrofit

import com.bit.kodari.Debate.CommentData.*
import com.bit.kodari.Debate.LikeData.CommentLikeRequest
import com.bit.kodari.Debate.LikeData.CommentLikeResponse
import com.bit.kodari.Debate.LikeData.PostLikeRequest
import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.*
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface DebateRetrofitInterface {
    @GET("/coins")              //토론장 코인 목록 조회
    fun getCoinsAll(@Header("X-ACCESS-TOKEN") jwt:String) : Call<DebateCoinResponse>

    @GET("/posts")
    fun getPostsAll(@Header("X-ACCESS-TOKEN") jwt:String) : Call<DebatePostResponse>

    //해당 코인에 대한 게시글들만 조회
    @GET("/posts/coin")
    fun getCoinPost(@Header("X-ACCESS-TOKEN") jwt:String, @Query("coinName") coinName: String) : Call<DebateCoinPostResponse>

    @POST("/posts/register")
    fun writePost(@Header("X-ACCESS-TOKEN") jwt:String, @Body updatePostRequest: DebateWritePostRequest) : Call<DebateWritePostResponse>

    //해당 게시글 정보 가져오기 .
    @GET("/posts/post")
    fun selectPost(@Header("X-ACCESS-TOKEN") jwt:String ,@Query("postIdx") postIdx : Int) : Call<DebateSelectPostResponse>

    @GET("/app/users/get")
    fun getUserInfo(@Query("userIdx") userIdx: Int) : Call<GetProfileResponse>

    //게시글 수정
    @PATCH("/posts/update/{postIdx}")
    fun updatePost(@Header("X-ACCESS-TOKEN") jwt:String , @Path("postIdx") postIdx:Int ,
    @Body debateModifyRequest : DebateModifyRequest) : Call<DebateModifyResponse>

    //게시글 삭제
    @PATCH("/posts/status/{postIdx}")
    fun deletePost(@Header("X-ACCESS-TOKEN") jwt:String , @Path("postIdx") postIdx:Int) : Call<DebateDeletePostResponse>

    //게시글 좋아요/싫어요 등록
    @POST("/likes/choose")
    fun postLike(@Header("X-ACCESS-TOKEN") jwt:String , @Body postLikeRequest: PostLikeRequest) : Call<PostLikeResponse>

    //댓글 등록하는 API
    @POST("/comments/register")
    fun regComment(@Header("X-ACCESS-TOKEN") jwt:String , @Body regCommentRequest: RegCommentRequest) : Call<RegCommentResponse>

    //댓글 수정 하는 API
    @PATCH("/comments/update/{postCommentIdx}")
    fun modifyComment(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("postCommentIdx") postCommentIdx:Int
    ,@Body modifyCommentRequest:ModifyCommentRequest) : Call<ModifyCommentResponse>

    //댓글 삭제하는 API
    @PATCH("/comments/status/{postCommentIdx}")
    fun deleteComment(@Header("X-ACCESS-TOKEN") jwt:String , @Path("postCommentIdx") postCommentIdx:Int): Call<DeleteCommentResponse>

    //토론장 대댓 등록
    @POST("/reply/register")
    fun regReComment(@Header("X-ACCESS-TOKEN") jwt:String , @Body regReCommentRequest: RegReCommentRequest) : Call<RegReCommentResponse>

    //토론장 대댓 삭제
    @PATCH("/reply/status/{postReplyIdx}")
    fun deleteReComment(@Header("X-ACCESS-TOKEN") jwt:String ,@Path("postReplyIdx") postReplyIdx: Int):Call<DeleteReCommentResponse>

    //댓글 좋아요 싫어요 API
    @POST("/comment/likes/choose")
    fun pressCommentLike(@Header("X-ACCESS-TOKEN") jwt : String ,@Body commentLikeRequest: CommentLikeRequest ) : Call<CommentLikeResponse>

}