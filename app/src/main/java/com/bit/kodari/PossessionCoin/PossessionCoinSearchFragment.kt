package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Debate.Adapter.DebateCoinRVAdapter
import com.bit.kodari.Debate.Data.DebateCoinResult
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinSearchAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinSearchView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinSearchBinding


class PossessionCoinSearchFragment : Fragment(), PsnCoinSearchView {
    lateinit var binding:FragmentPossessionCoinSearchBinding
    private var coinList = ArrayList<PsnCoinSearchResult>()
    private lateinit var possessionCoinSearchAdapter: PossessionCoinSearchAdapter

    override fun onStart() {
        super.onStart()
        getCoins()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentPossessionCoinSearchBinding.inflate(inflater , container , false)



        binding.tempDialogBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinAddFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinSearchBackIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }

//    fun setRecyclerView(){
//        possessionCoinSearchAdapter = PossessionCoinSearchAdapter(coinList)
//        //아이템 클릭 리스너를 현재 뷰에서 처리
//        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
////        possessionCoinSearchAdapter.setMyItemClickListener(object : DebateCoinRVAdapter.MyItemClickListener{
////            override fun onItemClick(item: DebateCoinResult) {      //이 아이템 클릭시 작동하게해야함
////                Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
////            }
////        })
//        binding.possessionCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        binding.possessionCoinSearchCoinListRV.adapter = possessionCoinSearchAdapter
//
//    }

    fun getCoins(){
        val psnCoinService = PsnCoinService()
        psnCoinService.setPsnCoinSearchView(this)
        psnCoinService.getCoinsAll()
    }

    override fun getCoinsAllSuccess(response: PsnCoinSearchResponse) {
        coinList=response.result as ArrayList<PsnCoinSearchResult>
        Log.d("getCoinsAllSuccess성공", "${coinList.size}")
        possessionCoinSearchAdapter = PossessionCoinSearchAdapter(coinList)
        binding.possessionCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinSearchCoinListRV.adapter= PossessionCoinSearchAdapter(coinList)
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("getCoinsAllFailure", "코인 목록 불러오기 실패, ${message}")
    }
}