package com.bit.kodari.Main.Adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bit.kodari.Main.MakePortfolioFragment
import com.bit.kodari.Main.MyPortfolioFragment

class HomeVPAdapter(fragment: Fragment , val portfolioList : ArrayList<Fragment>) : FragmentStateAdapter(fragment) {


    //생성 했을 때 무조건 1개는 존재하게 .
    init {
        portfolioList.add(MakePortfolioFragment())
    }

    override fun getItemCount(): Int = portfolioList.size

    override fun createFragment(position: Int): Fragment = portfolioList[position]

    fun addFragment(fragment: Fragment) {
        removeFragment(portfolioList.size - 1)        //마지막 생성 요구하는 프래그먼트 삭제
        Log.d("Viewpager" , "프래그먼트 추가 실행 , ${portfolioList.size}")
        portfolioList.add(fragment)                         //추가 포토폴리오 등록
        Log.d("Viewpager" , "프래그먼트 추가 실행 , ${portfolioList.size}")
        portfolioList.add(MakePortfolioFragment())          //다시 맨 뒤에 프래그먼트 입력
        Log.d("Viewpager" , "프래그먼트 만들기  추가 실행 , ${portfolioList.size}")
        for(i in portfolioList){
            Log.d("Viewpager", "${i.javaClass}")
        }
        notifyDataSetChanged()

    }

    fun removeFragment(position: Int) {
        Log.d("Viewpager" , "RemoveFragment 실행")
        portfolioList.removeAt(position)
        Log.d("Viewpager" , "${position}번째 삭제 ")
        //notifyItemRemoved(position)
        Log.d("Viewpager" , "RemoveFragment 끝 , ${portfolioList.size}")
        notifyItemRemoved(position)
    }

}