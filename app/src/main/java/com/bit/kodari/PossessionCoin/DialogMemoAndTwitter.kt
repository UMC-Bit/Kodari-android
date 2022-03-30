package com.bit.kodari.PossessionCoin

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinViewpagerAdapter
import com.bit.kodari.R
import com.bit.kodari.databinding.DialogMemoAndTwitterBinding
import com.google.android.material.tabs.TabLayoutMediator

class DialogMemoAndTwitter(val coinIdx:Int , val twitter: String) : DialogFragment(){

    lateinit var binding: DialogMemoAndTwitterBinding
    private val information = arrayListOf("메모", "트위터")
    lateinit var size : Point
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DialogMemoAndTwitterBinding.inflate(inflater , container , false)

        //뷰페이저와 탭 레이아웃 연결
        val possessionCoinAdapter = PossessionCoinViewpagerAdapter(this,coinIdx , twitter)
        binding.memoAndTwitterVp.adapter = possessionCoinAdapter
        binding.memoAndTwitterVp.isUserInputEnabled = false
        TabLayoutMediator(binding.memoAndTwitterTb, binding.memoAndTwitterVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()

        getDeviceSize()         //디바이스 사이즈 구하기.

        return binding.root
    }
    //디바이스 크기 구하기
    fun getDeviceSize(){
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        size = Point()
        display.getSize(size)

    }

    override fun onResume() {
        super.onResume()
        //여백 비율로 DialogFragment 크기 조절
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceHeight* 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
//
//        dialog!!.window!!.setLayout(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container_fl, HomeFragment()).commit()
    }

}