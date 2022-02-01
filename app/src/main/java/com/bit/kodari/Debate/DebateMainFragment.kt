package com.bit.kodari.Debate

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.DebateMainRVAdapter
import com.bit.kodari.Debate.Adapter.MyWritingRVAdapter
import com.bit.kodari.Debate.Data.DebatePostResponse
import com.bit.kodari.Debate.Data.DebatePostResult
import com.bit.kodari.Debate.Retrofit.DebateMainView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.databinding.FragmentDebateMainBinding

class DebateMainFragment : BaseFragment<FragmentDebateMainBinding>(FragmentDebateMainBinding::inflate) , DebateMainView{

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

    fun setListener(){
        //코인 검색 창 뜨게 하기
        binding.debateMainInputCl.setOnClickListener {
            val dialog = DialogCoin()
            dialog.show(requireActivity().supportFragmentManager , "DialogCoin")
        }

    }

    fun setRecyclerView(){
        debateMainRVAdapter = DebateMainRVAdapter(postList)
        binding.debateMainListRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false)
        binding.debateMainListRv.adapter = debateMainRVAdapter
    }

    override fun getPostsAllSuccess(response: DebatePostResponse) {
        Log.d("debate" , "호출 성공 2 ${response.code}")
        dismissLoadingDialog()
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
    }

    override fun getPostsAllFailure(msg:String) {
        Log.d("getPost",msg)
    }


}