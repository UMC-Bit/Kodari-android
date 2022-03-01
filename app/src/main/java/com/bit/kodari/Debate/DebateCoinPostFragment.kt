package com.bit.kodari.Debate

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.DebateCoinPostRVAdapter
import com.bit.kodari.Debate.PostData.DebateCoinPostResponse
import com.bit.kodari.Debate.PostData.DebateCoinPostResult
import com.bit.kodari.Debate.Retrofit.DebateCoinPostView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentDebateCoinPostBinding


class DebateCoinPostFragment : BaseFragment<FragmentDebateCoinPostBinding>(FragmentDebateCoinPostBinding::inflate) , DebateCoinPostView {

    //argument로 데이터 받기
    private lateinit var coinName :String
    private var coinIdx = 0             //초기값
    private lateinit var debateCoinPostRVAdapter: DebateCoinPostRVAdapter
    private lateinit var coinPostList : ArrayList<DebateCoinPostResult>

    override fun initAfterBinding() {
        getCoinName()       //이름 가져오기
        getCoinIndex()      //코인 인덱스 가져오기
        //가져온 이름으로 API 호출 밑 View 셋팅
        setListener()
        binding.debateCoinMainNameTv.text = coinName
        //API 호출
        val debateService = DebateService()
        debateService.setDebateCoinPostView(this)
        showLoadingDialog(requireContext())
        debateService.getCoinPost(coinName) //코인 별 포스트 검색
    }

    fun getCoinName(){
        if(requireArguments().containsKey("coinName")){
            coinName = requireArguments().getString("coinName")!!
        }
    }

    fun getCoinIndex(){
        if(requireArguments().containsKey("coinIdx")){
            coinIdx = requireArguments().getInt("coinIdx")
        }
        Log.d("CoinPost" , "${coinIdx}")
    }

    fun setListener(){
        //글쓰기 버튼 눌렀을 경우
        binding.debateCoinMainWriteBtn.setOnClickListener {
            val tempCoinName = coinName
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, DebatePostWriteFragment().apply {
                    arguments = Bundle().apply {
                        putString("coinName", tempCoinName)
                        putInt("coinIdx" , coinIdx)
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.debateCoinMainFindCoinCl.setOnClickListener {
            val dialog = DialogCoin()
            dialog.show(requireActivity().supportFragmentManager , "DialogCoin")
        }
    }

    fun setRecyclerView(){
        //더미 데이터 셋팅
        //coinPostList = ArrayList<DebateCoinPostResult>()
        //coinPostList.add(DebateCoinPostResult(3,"더미데이터입니다.",5,5,"wd78wg","","DOGE","1시간전"))
        debateCoinPostRVAdapter = DebateCoinPostRVAdapter(coinPostList)
        debateCoinPostRVAdapter.setMyItemClickListener(object :DebateCoinPostRVAdapter.MyItemClickListener{
            override fun onItemClick(item: DebateCoinPostResult) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, DebateMineFragment(2,coinName).apply {
                        arguments = Bundle().apply {
                            putInt("postIdx" , item.postIdx)
                            putInt("coinIdx", coinIdx)
                        }
                    }).addToBackStack(null).commitAllowingStateLoss()
            }
        })
        binding.debateCoinMainListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL , false)
        binding.debateCoinMainListRv.adapter = debateCoinPostRVAdapter
    }

    override fun getCoinPostSuccess(response: DebateCoinPostResponse) {
        coinPostList = response.result
        Log.d("coinPost" ,"성공 , ${response.result.size}")
        setRecyclerView()
        dismissLoadingDialog()
    }

    override fun getCoinPostFailure(message: String) {
        Log.d("coinPost" ,"성공 ,${message}")
        dismissLoadingDialog()
    }
}