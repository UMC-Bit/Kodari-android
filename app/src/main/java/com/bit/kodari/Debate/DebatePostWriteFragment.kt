package com.bit.kodari.Debate

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Data.DebateWritePostRequest
import com.bit.kodari.Debate.Data.DebateWritePostResponse
import com.bit.kodari.Debate.Retrofit.DebatePostWriteVIew
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentDebatePostWriteBinding
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

//글 입력할때 프로필이랑 뜨게해야함
//닉네임도 셋팅해야함
class DebatePostWriteFragment : BaseFragment<FragmentDebatePostWriteBinding>(FragmentDebatePostWriteBinding::inflate) ,DebatePostWriteVIew {
    private var coinIdx by Delegates.notNull<Int>()         //어떤 코인 게시글에 쓸지

    override fun initAfterBinding() {
        setInit()
        setListener()
    }

    //초기 기본 셋팅 값들 설정
    fun setInit(){
        //인스타그램처럼 보내기 , 자동 개행 기능 부여
        binding.postWriteContentEt.imeOptions = EditorInfo.IME_ACTION_SEND
        binding.postWriteContentEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        getCoinName()
        getCoinIdx()            //코인 인덱스 얻기
        //유저 Index로 유저 정보 얻어와야함.     ->그래야지 셋팅 가능 , 닉네임 프로필 사진 등 얻어오는 과정.
        val debateService = DebateService()
        debateService.setDebatePostWriteVIew(this)
        showLoadingDialog(requireContext())
        debateService.getUserInfo()
    }

    fun getCoinName(){
        if(requireArguments().containsKey("coinName")){         //코인 이름 셋팅
            var coinName = requireArguments().getString("coinName")!!
            binding.postWriteSymbolTv.text = coinName
        }
    }

    fun getCoinIdx(){
        if(requireArguments().containsKey("coinIdx")){
            coinIdx = requireArguments().getInt("coinIdx")
        }
        Log.d("Post coinIdx" , "${coinIdx}")
    }


    fun setListener(){
        //다른 곳 클릭시 올라온 키보드 내려가게함
        binding.debatePostWriteRoot.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }

        binding.postWriteCompleteBtnTv.setOnClickListener {
            //게시글 올리기 API 활성화
            var post = DebateWritePostRequest(coinIdx, getUserIdx(),binding.postWriteContentEt.text.toString())
            Log.d("post" , "${coinIdx} , ${getUserIdx()} ,${binding.postWriteContentEt.text.toString()} ")
            val debateService = DebateService()
            debateService.setDebatePostWriteVIew(this)
            showLoadingDialog(requireContext())
            debateService.writePost(post)

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

    //글쓰기 API 정상적으로 호출됐을때.
    override fun updatePostSuccess(response: DebateWritePostResponse) {
        dismissLoadingDialog()
        when(response.code){
          1000 -> {
              showToast("글쓰기에 성공하셨습니다.")
              Log.d("post" , "${response.result.userIdx}")
          }
          else -> {
              showToast(response.message)
              Log.d("post" , "글쓰기에 실패했습니다. ${response.code} , ${response.message} , ${response.isSuccess}")
          }
        }
    }

    //글쓰기 API 정상적으로 아닐떄떄
    override fun updatePostFailure(message: String) {
        dismissLoadingDialog()
        showToast("통신실패")
    }

    //유저 정보 받아오기 성공
    override fun getUserInfoSuccess(response: GetProfileResponse) {
        dismissLoadingDialog()
        Glide.with(binding.postWriteProfileIv)
            .load(response.result[0].profileImgUrl)                 //왜 리스트지 ?
            .error(R.drawable.profile_image)
            .into(binding.postWriteProfileIv)

        binding.postWriteNicknameTv.text = response.result[0].nickName  //닉네임 셋팅.
    }

    //유저 정보 받아오기 실패
    override fun getUserInfoFailure(message: String) {
        dismissLoadingDialog()
        Log.d("WriteGetUserInfo" , "${message}")
    }
}