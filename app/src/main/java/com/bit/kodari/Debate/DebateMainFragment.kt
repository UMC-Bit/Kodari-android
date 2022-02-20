package com.bit.kodari.Debate

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.DebateMainRVAdapter
import com.bit.kodari.Debate.LikeData.PostLikeRequest
import com.bit.kodari.Debate.LikeData.PostLikeResponse
import com.bit.kodari.Debate.PostData.DebatePostResponse
import com.bit.kodari.Debate.PostData.DebatePostResult
import com.bit.kodari.Debate.Retrofit.DebateMainView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentDebateMainBinding

class DebateMainFragment : BaseFragment<FragmentDebateMainBinding>(FragmentDebateMainBinding::inflate) , DebateMainView{
    private var checkView = true
    private var postList = ArrayList<DebatePostResult>()
    private lateinit var debateMainRVAdapter : DebateMainRVAdapter

    override fun initAfterBinding() {
        setListener()
        Log.d("debate" , "onStart실행")
        val debateService = DebateService()
        debateService.setDebateMainView(this)
        showLoadingDialog(requireContext())
        debateService.getPostsAll()
    }

    override fun onDestroyView() {
        checkView = false
        super.onDestroyView()
    }

    fun setListener(){
        //코인 검색 창 뜨게 하기
        binding.debateMainFindCoinCl.setOnClickListener {
            val dialog = DialogCoin()
            dialog.show(requireActivity().supportFragmentManager , "DialogCoin")
        }

    }

    fun setRecyclerView() {
        if (checkView) {
            debateMainRVAdapter = DebateMainRVAdapter(postList)
            debateMainRVAdapter.setMyItemClickListener(object :
                DebateMainRVAdapter.MyItemClickListener {
                override fun onItemClick(item: DebatePostResult) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, DebateMineFragment(1).apply {
                            arguments = Bundle().apply {
                                putInt("postIdx", item.postIdx)
                            }
                        }).addToBackStack(null).commitAllowingStateLoss()
                }

            })
            binding.debateMainListRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.debateMainListRv.adapter = debateMainRVAdapter
        }
    }

    override fun getPostsAllSuccess(response: DebatePostResponse) {
        Log.d("debate" , "호출 성공 2 ${response.code}")
        when(response.code){
          1000 -> {
              postList = response.result as ArrayList<DebatePostResult>
              Log.d("getPost","${postList.size}")
              setRecyclerView()
          }
          else -> {
              Log.d("getPost","${response.message}")
              Toast.makeText(requireContext(), "통신 실패" , Toast.LENGTH_SHORT).show()
          }
        }
        dismissLoadingDialog()
    }

    override fun getPostsAllFailure(msg:String) {
        Log.d("getPost",msg)
        dismissLoadingDialog()
    }


}