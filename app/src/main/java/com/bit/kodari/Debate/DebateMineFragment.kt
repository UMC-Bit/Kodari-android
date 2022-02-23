package com.bit.kodari.Debate

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.DebateCommentRVAdapter
import com.bit.kodari.Debate.Adapter.DebateReCommentRVAdapter
import com.bit.kodari.Debate.CommentData.*
import com.bit.kodari.Debate.LikeData.CommentLikeRequest
import com.bit.kodari.Debate.LikeData.CommentLikeResponse
import com.bit.kodari.Debate.LikeData.PostLikeRequest
import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.DebateSelectPostComment
import com.bit.kodari.Debate.PostData.DebateSelectPostReply
import com.bit.kodari.Debate.PostData.DebateSelectPostResponse
import com.bit.kodari.Debate.Retrofit.DebateMineView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Login.Retrofit.ProfileRetrofitInterface
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getRetorfit
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentDebateMineBinding
import com.bit.kodari.databinding.ListCommentBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//게시글 보여주는 화면
//해야할 거 : 리싸이클러뷰 어댑터 구현 (1순위) -> DebateSelectPostComment 들이 리싸이클러뷰로 옴
//DebateSelectPostResult로 현재 게시글 셋팅
class DebateMineFragment(val flag:Int , var coinName:String ="") : BaseFragment<FragmentDebateMineBinding>(FragmentDebateMineBinding::inflate) , DebateMineView{

    private lateinit var commentList : ArrayList<DebateSelectPostComment>
    var temp = ArrayList<DebateSelectPostReply>()
    private lateinit var debateCommentRVAdapter: DebateCommentRVAdapter
    private var imgUrl : String? =null
    private var postIdx = 0
    private var commentIdx = 0
    private var coinIdx = 0

    override fun initAfterBinding() {
        setInit()
        setListener()
        callSelectPost()
        callMyProfile()
        Log.d("nowPostIdx" , "지금 게시판의 postIdx : ${postIdx}")
        if(requireArguments().containsKey("coinIdx")){
            coinIdx = requireArguments().getInt("coinIdx")
            Log.d("rere" , "${coinIdx}")
        }
    }

    //초기 기본 셋팅 값들 설정
    fun setInit(){
        //인스타그램처럼 보내기 , 자동 개행 기능 부여
        binding.mineCommentInputMessageEt.imeOptions = EditorInfo.IME_ACTION_SEND
        binding.mineCommentInputMessageEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        //넘어온 postIdx 받기
        if(requireArguments().containsKey("postIdx")){
            postIdx = requireArguments().getInt("postIdx")
            Log.d("selectPost" , "넘어온 postIdx : ${postIdx}")
        } else{
            showToast("통신 실패")
        }
    }

    //초기 리스너들 설정
    fun setListener() {
        //다른 곳 클릭시 올라온 키보드 내려가게함
        binding.debateMineRoot.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }

        binding.mineCommentInputMessageEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {        //답글 입력했을때만 버튼 클릭할 수 있게함
                if (editable?.length!! > 0) {
                    binding.mineCommentSendBtn.isEnabled = true
                    binding.mineCommentSendBtn.setBackgroundResource(R.drawable.btn_outline_yellow);
                    binding.mineReCommentSendBtn.isEnabled = true             //대댓 다는 버튼 -> 답글달기 눌렀을떄 활성화하게해줘야함.
                    binding.mineReCommentSendBtn.setBackgroundResource(R.drawable.btn_outline_yellow)
                } else {
                    binding.mineCommentSendBtn.isEnabled = false
                    binding.mineCommentSendBtn.setBackgroundResource(R.drawable.btn_outline_white);
                    binding.mineReCommentSendBtn.isEnabled = false
                    binding.mineReCommentSendBtn.setBackgroundResource(R.drawable.btn_outline_white)
                }
            }
        })

        //삭제 버튼 눌렀을 때 , 다이얼로그 창 띄워줌
        binding.mineRemoveTv.setOnClickListener {
            val tempPost = postIdx          //전역으로 선언된 postIdx를 넘겨주기 위해서 사용 ->근데 왜 Bundle()에서는 안될까?
            val dialog = DeleteDialog().apply {
                arguments = Bundle().apply {
                    putInt("postIdx", tempPost)
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "DeleteDialog")

            //postIdx 넘겨줘야함
        }

        //수정 버튼 눌렀을 때 , 닉네임, 내용 , ImgUrl, postIdx 넘겨줘야함
        binding.mineModifyTv.setOnClickListener {
            //게시글별 조회해서 필요한 내용만 가져다 사용 , 게시글 수정에서 왜 coinIdx가 필요해 ?
            val tempPost = postIdx          //전역으로 선언된 postIdx를 넘겨주기 위해서 사용 ->근데 왜 Bundle()에서는 안될까?
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, DebateModifyPostFragment(coinName , coinIdx).apply {
                    arguments = Bundle().apply {
                        putInt("postIdx", tempPost)
                        Log.d("postIdx", "넘기는 : ${tempPost}")
                        putString("imgUrl", imgUrl)
                        putString("nickName", binding.mineNicknameTv.text.toString())
                        putString("content", binding.mineContentTv.text.toString())
                        putInt("flag",flag)
                    }
                }).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.mineLikeBtn.setOnClickListener {
            //좋아요 버튼 클릭 1이 좋아요 0이 싫어요
            callPostLike(1)
        }

        binding.mineNoLikeBtn.setOnClickListener {
            //싫어요 버튼 클릭릭
            callPostLike(0)
        }

        //댓글 달기 버튼
        binding.mineCommentSendBtn.setOnClickListener {
            //댓글 달기 버튼 활성화되어서 눌렀을 경우.
            callRegComment()
        }

        //대댓 달기 버튼
        binding.mineReCommentSendBtn.setOnClickListener {
            //대댓 달기 API 호출
            callRegReComment(commentIdx)
            //초기화 -> 다시 호출하면 초기화되나 ? 뷰를 다시그리나 ? 아닐 것 같음
            binding.mineCommentSendBtn.visibility =View.VISIBLE
            binding.mineReCommentSendBtn.visibility = View.GONE          //대댓 보내기 활성화
            binding.mineCommentInputMessageEt.hint = "해당 게시글에 댓글을 달아주세요."
            binding.mineCommentInputMessageEt.text.clear()

        }

        binding.mineBackBtnTv.setOnClickListener {
            if(flag == 1){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , DebateMainFragment()).commit()
            } else{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , DebateCoinPostFragment().apply {
                        arguments = Bundle().apply {
                            putString("coinName", coinName)
                            putInt("coinIdx" , coinIdx)
                        }
                    }).commit()
            }
        }
    }

    //다른 곳 클릭시 올라온 키보드 내려가게함
    fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    //리싸이클러뷰 셋팅.
    fun setRecyclerView(){
        debateCommentRVAdapter = DebateCommentRVAdapter(commentList)
        debateCommentRVAdapter.setMyItemClickListener(object :DebateCommentRVAdapter.MyItemClickListener{
            override fun onModifyClick(item: DebateSelectPostComment) {     //수정 버튼 클릭시
                //댓글 수정 API 호출 , 어디서 수정을 진행하지 ?
            }

            override fun onDeleteClick(item: DebateSelectPostComment) {     //삭제 버튼 클릭 시
                //댓글 삭제 API 호출
                callDeleteComment(item.postCommentIdx)          //해당 댓글 삭제
            }

            override fun onItemClick(binding: ListCommentBinding) {         //댓글 아이템 클릭시


            }

            //대댓달기 위해 답글달기 버튼 눌렀을 때 대댓 버튼 활성화 , Hint 부여 , EditText에 포커스
            override fun onRegReComment(item: DebateSelectPostComment) {
                binding.mineCommentSendBtn.visibility =View.GONE
                binding.mineReCommentSendBtn.visibility = View.VISIBLE          //대댓 보내기 활성화
                binding.mineCommentInputMessageEt.hint = "해당 댓글에 답글을 입력해주세요."
                //EditText에 포커스 오게하기.
                binding.mineCommentInputMessageEt.requestFocus()
                val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.mineCommentInputMessageEt , 0)        //자동으로 키보드 올라오게 하기
                commentIdx = item.postCommentIdx
                Log.d("commentIdx" , "commentIdx 는  ${commentIdx}")
            }

            override fun onClickReComment(postReplyIdx: Int) {      //ReComment에서 Delete 눌렀을떄 작동
                callDeleteReComment(postReplyIdx)
            }

            override fun onLikeClick(item: DebateSelectPostComment) {       //좋아요 눌렀을시 API 호출
                val commentLikeRequest = CommentLikeRequest( item.postCommentIdx , getUserIdx())
                Log.d("like", "댓글 index : ${item.postCommentIdx} , 유저인덱스 : ${getUserIdx()}")
                callPressCommentLike(commentLikeRequest)
            }
        })

        binding.mineCommentRv.layoutManager = LinearLayoutManager(context ,LinearLayoutManager.VERTICAL ,false)
        binding.mineCommentRv.adapter = debateCommentRVAdapter
    }
    //게시글 정보 불러오기
    override fun selectPostSuccess(response: DebateSelectPostResponse) {
        val post = response.result                                  //전달 받은 게시물 정보
        binding.mineContentTv.text = post.content.toString()
        binding.mineNicknameTv.text = post.nickName
        binding.mineCommentCountNumTv.text = post.comment_cnt.toString()
        binding.mineLikeNumTv.text = post.like.toString()
        binding.mineNoLikeNumTv.text = post.dislike.toString()
        binding.mineCoinSymbolTv.text = post.symbol
        if(!post.checkWriter){                                  //체크 writer가 false 면 버튼 안보이게해야함
            binding.mineOutlineTV.visibility = View.GONE
            binding.mineModifyTv.visibility = View.GONE
            binding.mineRemoveTv.visibility = View.GONE
        }
        //이미지 그리기 .
        Glide.with(binding.mineMaskIv)
            .load(post.profileImgUrl)
            .error(R.drawable.profile_image)
            .into(binding.mineMaskIv)

        if(imgUrl != null){     //null 아닐때만 넘겨주
            imgUrl = post.profileImgUrl.toString()   //이미지 URL 저장 , 이거를 수정 눌렀을떄 넘겨주면됨됨
        }


        commentList = post.commentList          //댓글들 셋팅
        Log.d("commentList", "commentList 사이즈 : ${commentList.size}")
        setRecyclerView()                       //댓글 recyclerView셋팅
        dismissLoadingDialog()
    }

    override fun selectPostFailure(message: String) {
        Log.d("selectPost" , "통신 실패 : $message")
    }

    //좋아요 눌렀을 때 성공 시 처리
    override fun postLikeSuccess(response: PostLikeResponse , like:Int) {
        //임시로 올라간 것 같이 처리 -> 나중에 조회해오면 바로 반영되어 있음
        if(like == 1) {     //좋아요 눌렀으면.
            //좋아요 버튼 누른 후 게시물 재 조회 해서 새로 반영
            callSelectPost()        //게시글 조회 API
        } else{
            callSelectPost()
        }
    }

    //좋아요 눌렀을 때 실패 시 처리리
    override fun postLikeFailure(message: String) {
        Log.d("postLike" ,"통신 실패 : $message")
    }

    //댓글 등록하는 API
    override fun regCommentSuccess(response: RegCommentResponse) {
        when(response.code){
          1000 -> {
              callSelectPost()
              binding.mineCommentInputMessageEt.text.clear()
          }        //성공했을 때 재조회로 내용 갱신
          else -> {
              showToast(response.message)
          }
        }

    }

    override fun regCommentFailure(message: String) {
        showToast(message)
        Log.d("regComment" , "$message")
    }

    //댓글 수정 성공 시에 호출 호 출 이후 다시 재정의
    override fun modifyCommentSuccess(response: ModifyCommentResponse) {
        showToast("${response.message}")
        callSelectPost()
    }

    //댓글 수정 실패시에 호출
    override fun modifyCommentFailure(message: String) {
        showToast(message)
        callSelectPost()
    }

    //댓글 삭제 성공 시에 호출
    override fun deleteCommentSuccess(response: DeleteCommentResponse) {
        when(response.code){
          1000 -> {
              showToast("댓글을 삭제되었습니다.")
          }
          else -> {
              showToast(response.result)
          }
        }
        callSelectPost()
    }

    //댓글 삭제 실패 시에 호출
    override fun deleteCommentFailure(message: String) {
        showToast("${message}")
        callSelectPost()                //수정 ,삭제 작업 종료후 조회된 글불러와서 재정의
    }

    //대댓 등록 성공
    override fun regReCommentSuccess(response: RegReCommentResponse) {
        when(response.code){
          1000 -> {
              showToast("답글을 등록했습니다.")
              callSelectPost()
          }
          else -> {
              showToast(response.message)
              callSelectPost()
          }
        }
    }
    //대댓 등록 실패
    override fun regReCommentFailure(message: String) {
        showToast(message)
    }

    //대댓 삭제 성공
    override fun deleteReCommentSuccess(response: DeleteReCommentResponse) {
        when(response.code){
          1000 -> {
              showToast(response.result)
              callSelectPost()
          }
          else -> {
              showToast(response.message)
              callSelectPost()
          }
        }
    }

    //대댓 삭제 실패
    override fun deleteReCommentFailure(message: String) {
        showToast(message)
    }

    //좋아요 버튼 누르기 성공
    override fun pressCommentLikeSuccess(response: CommentLikeResponse) {
        //코드 1004면 좋아요 1003이면 해제
        showToast(response.message)
    }
    //좋아요 버튼 누르기 실패
    override fun pressCommentLikeFailure(message: String) {
        showToast(message)
    }

    //게시글 조회 함수
    fun callSelectPost(){
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        showLoadingDialog(requireContext())
        debateService.selectPost(postIdx)
    }

    //게시글 좋아요 누르기
    fun callPostLike(like:Int){
        val postLikeRequest = PostLikeRequest(like,postIdx, getUserIdx())
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.postLike(postLikeRequest , like)
    }

    fun callRegComment(){
        val regCommentRequest = RegCommentRequest(binding.mineCommentInputMessageEt.text.toString() , postIdx , getUserIdx())
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.regComment(regCommentRequest)
    }

    //프로필 이미지 가져오기
    fun callMyProfile(){
        Log.d("getProfile", "실행")
        val profileService = getRetorfit().create(ProfileRetrofitInterface::class.java)
        profileService.getProfile(getUserIdx()).enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                Log.d("getProfile", "실행 성공 ${response.body()!!.code}")
                when(response.body()!!.code){
                  1000 -> {
                      Glide.with(binding.mineCommentInputProfileIv)
                          .load(response.body()!!.result[0].profileImgUrl)
                          .error(R.drawable.profile_image)
                          .into(binding.mineCommentInputProfileIv)
                  }
                  else -> {
                      Log.d("getProfile" , "${response.body()!!.message}")
                      showToast(response.message())
                  }
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                Log.d("getProfile", "실행 실패")
                Log.d("getProfile" , "${t}")
                showToast("${t}")
            }
        })
    }

    //답글 삭제하는 함수
    fun callDeleteComment(postCommentIdx: Int){
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.deleteComment(postCommentIdx)
    }

    //대댓 등록하는 함수
    fun callRegReComment(commentIdx: Int){

        val regRecommentRequest = RegReCommentRequest(binding.mineCommentInputMessageEt.text.toString() ,commentIdx,
            getUserIdx() )
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.regReComment(regRecommentRequest)
    }

    //대댓 삭제 호출 함수
    fun callDeleteReComment(postReplyIdx: Int){
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.deleteReComment(postReplyIdx)
    }

    //댓글 좋아요 누르기
    fun callPressCommentLike(commentLikeRequest: CommentLikeRequest){
        Log.d("like", "${commentLikeRequest}")
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        debateService.pressCommentLike(commentLikeRequest)
    }

}