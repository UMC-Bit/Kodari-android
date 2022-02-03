package com.bit.kodari.Debate

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.DebateCommentRVAdapter
import com.bit.kodari.Debate.Adapter.DebateMainRVAdapter
import com.bit.kodari.Debate.Data.DebateSelectPostComment
import com.bit.kodari.Debate.Data.DebateSelectPostResponse
import com.bit.kodari.Debate.Retrofit.DebateMineView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentDebateMineBinding
import com.bumptech.glide.Glide


//게시글 보여주는 화면
//해야할 거 : 리싸이클러뷰 어댑터 구현 (1순위) -> DebateSelectPostComment 들이 리싸이클러뷰로 옴
//DebateSelectPostResult로 현재 게시글 셋팅
class DebateMineFragment : BaseFragment<FragmentDebateMineBinding>(FragmentDebateMineBinding::inflate) , DebateMineView{

    private lateinit var commentList : ArrayList<DebateSelectPostComment>
    private lateinit var debateCommentRVAdapter: DebateCommentRVAdapter
    private var imgUrl : String? =null
    private var postIdx = 0

    override fun initAfterBinding() {
        setListener()
        setInit()
        val debateService = DebateService()
        debateService.setDebateMineView(this)
        showLoadingDialog(requireContext())
        debateService.selectPost(postIdx)                          //postIdx 넘겨받고해야함.

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
    fun setListener(){
        //다른 곳 클릭시 올라온 키보드 내려가게함
        binding.debateMineRoot.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }
        binding.mineCommentInputMessageEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {        //답글 입력했을때만 버튼 클릭할 수 있게함
                if (editable?.length!! > 0) {
                    binding.mineCommentSendBtn.setClickable(true);
                    binding.mineCommentSendBtn.setBackgroundColor(Color.BLUE);
                } else {
                    binding.mineCommentSendBtn.setClickable(false);
                    binding.mineCommentSendBtn.setBackgroundColor(Color.GRAY);
                }
            }
        })

        //삭제 버튼 눌렀을 때 , 다이얼로그 창 띄워줌
        binding.mineRemoveTv.setOnClickListener {
            val dialog = DeleteDialog(requireContext())
            dialog.showDiaglog()            //만든 다이얼로그 띄워주기
        }

        //수정 버튼 눌렀을 때 , 닉네임, 내용 , ImgUrl, postIdx 넘겨줘야함
       binding.mineModifyTv.setOnClickListener {
            //게시글별 조회해서 필요한 내용만 가져다 사용 , 게시글 수정에서 왜 coinIdx가 필요해 ?
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , DebateModifyPostFragment().apply {
                    arguments = Bundle().apply {
                        putInt("postIdx" , postIdx)
                        putString("imgUrl" , imgUrl)
                        putString("nickName" , binding.mineNicknameTv.text.toString())
                        putString("content",binding.mineContentTv.text.toString())
                    }
                }).addToBackStack(null).commitAllowingStateLoss()
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

    fun setRecyclerView(){
        debateCommentRVAdapter = DebateCommentRVAdapter(commentList)
        binding.mineCommentRv.layoutManager = LinearLayoutManager(context ,LinearLayoutManager.VERTICAL ,false)
        binding.mineCommentRv.adapter = debateCommentRVAdapter
    }

    override fun selectPostSuccess(response: DebateSelectPostResponse) {
        dismissLoadingDialog()
        val post = response.result                                  //전달 받은 게시물 정보
        binding.mineContentTv.text = post.content.toString()
        binding.mineNicknameTv.text = post.nickName
        binding.mineCommentCountNumTv.text = post.comment_cnt.toString()
        binding.mineLikeNumTv.text = post.like.toString()
        binding.mineNoLikeNumTv.text = post.dislike.toString()
        binding.mineCoinSymbolTv.text = post.symbol
        if(!post.checkWriter){                                  //체크 writer가 false 면 버튼 안보이게해야함
            binding.mineBtnContainerLl.visibility = View.GONE
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
        setRecyclerView()                       //댓글 recyclerView셋팅
    }

    override fun selectPostFailure(message: String) {
        Log.d("selectPost" , "통신 실패 : $message")
    }
}