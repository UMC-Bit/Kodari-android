package com.bit.kodari.Profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Adapter.MyCommentRVAdapter
import com.bit.kodari.Profile.Retrofit.MyCommentView
import com.bit.kodari.Profile.RetrofitData.GetMyCommentResponse
import com.bit.kodari.Profile.RetrofitData.GetMyCommentResult
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentMyCommentBinding

class MyCommentFragment : BaseFragment<FragmentMyCommentBinding>(FragmentMyCommentBinding::inflate) , MyCommentView {

    private var postList = ArrayList<GetMyCommentResult>()
    private lateinit var myCommentRVAdapter: MyCommentRVAdapter

    override fun initAfterBinding() {

        val profileService = ProfileService()
        profileService.setMyCommentView(this)
        showLoadingDialog(requireContext())
        profileService.getMyComment(getUserIdx())

        setListener()
    }

    fun setListener() {
        binding.myCommentPreIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, ProfileMainFragment()).addToBackStack(null).commitAllowingStateLoss()
        }
    }

    fun setRecylcerView(){
        myCommentRVAdapter = MyCommentRVAdapter(postList)
        binding.myCommentListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.myCommentListRv.adapter = myCommentRVAdapter
    }



    override fun getMyCommentSuccess(response: GetMyCommentResponse) {
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

    override fun getMyCommentFailure(message: String) {
        Log.d("getMyComment", "불러오기 실패")
    }
}