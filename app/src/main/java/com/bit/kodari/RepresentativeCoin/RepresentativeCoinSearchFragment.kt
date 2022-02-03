package com.bit.kodari.RepresentativeCoin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.PossessionCoinAddFragment
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Adapter.RptCoinSearchRVAdapter
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinSearchView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResult
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.databinding.FragmentRepresentativeCoinSearchBinding

class RepresentativeCoinSearchFragment : BaseFragment<FragmentRepresentativeCoinSearchBinding>(
    FragmentRepresentativeCoinSearchBinding::inflate), RptCoinSearchView {

    private var coinList = ArrayList<RptCoinSearchResult>()
    private var filteredList  = ArrayList<RptCoinSearchResult>()
    private lateinit var rptCoinSearchRVAdapter: RptCoinSearchRVAdapter

    override fun initAfterBinding() {
        getCoins()
        setListeners()

        binding.representativeCoinSearchBackIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
        }
    }

    fun setRecyclerView(){
        rptCoinSearchRVAdapter = RptCoinSearchRVAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        rptCoinSearchRVAdapter.setMyItemClickListener(object : RptCoinSearchRVAdapter.MyItemClickListener{
            override fun onItemClick(item: RptCoinSearchResult) {      //이 아이템 클릭시 작동하게해야함
//                Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinAddFragment().apply {
                        arguments = Bundle().apply {
                            putString("coinImage",item.coinImg)
                            putString("coinName", item.coinName)
                            putString("coinSymbol", item.symbol)
                        }
                    }).commitAllowingStateLoss()
            }
        })

        binding.representativeCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinSearchCoinListRV.adapter= rptCoinSearchRVAdapter
    }

    fun setListeners(){
        binding.representativeCoinSearchSearchInputET.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("setlistener", "실행")
                var searchText =  binding.representativeCoinSearchSearchInputET.text.toString()
                searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        Log.d("searchFilter", "실행")
        filteredList.clear()
        for (i in 0 until coinList.size) {
            if (coinList[i].coinName.toLowerCase()
                    .contains(searchText.toLowerCase())
            ) {
                filteredList.add(coinList[i])
            }
        }
        rptCoinSearchRVAdapter.filterList(filteredList)
    }

    fun getCoins(){
        val rptCoinService = RptCoinService()
        rptCoinService.setRptCoinSearchView(this)
        rptCoinService.getCoinsAll()
    }

    override fun getCoinsAllSuccess(response: RptCoinSearchResponse) {
        coinList=response.result as ArrayList<RptCoinSearchResult>
        Log.d("getCoinsAllSuccess성공", "${coinList.size}")

        setRecyclerView()
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("getCoinsAllFailure", "코인 목록 불러오기 실패, ${message}")
    }


}