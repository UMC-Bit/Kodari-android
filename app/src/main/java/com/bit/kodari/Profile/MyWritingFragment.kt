package com.bit.kodari.Profile

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Debate.Adapter.MyWritingRVAdapter
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.MyPostView
import com.bit.kodari.Profile.RetrofitData.GetMyPostResponse
import com.bit.kodari.Profile.RetrofitData.GetMyPostResult
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentMyWritingBinding

class MyWritingFragment : BaseFragment<FragmentMyWritingBinding>(FragmentMyWritingBinding::inflate) , MyPostView {

    private var postList = ArrayList<GetMyPostResult>()
    private lateinit var myWritingRVAdapter : MyWritingRVAdapter

    override fun initAfterBinding() {

        val profileService = ProfileService()
        profileService.setMyPostView(this)
        showLoadingDialog(requireContext())
        profileService.getMyPost(getUserIdx())

        setListener()
    }


    fun setListener() {
        binding.myWritingPreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).commitAllowingStateLoss()
        }
    }

    fun setRecylcerView(){
        myWritingRVAdapter = MyWritingRVAdapter(postList)
        binding.myWritingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.myWritingListRv.adapter = myWritingRVAdapter
    }

    override fun getMyPostSuccess(response: GetMyPostResponse) {
        dismissLoadingDialog()
        when(response.code) {
            1000 -> {
                postList = response.result
                setRecylcerView()
            }
            else -> {
                Toast.makeText(requireContext(), "통신 실패" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getMyPostFailure(message: String) {
        Log.d("getMyPost" , "불러오기 실패")
    }




}