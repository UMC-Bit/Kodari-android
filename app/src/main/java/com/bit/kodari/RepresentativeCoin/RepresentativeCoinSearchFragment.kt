package com.bit.kodari.RepresentativeCoin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.PossessionCoinAddFragment
import com.bit.kodari.PossessionCoin.PossessionCoinManagementFragment
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.RepresentativeCoin.Adapter.RptCoinSearchRVAdapter
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinAddView
import com.bit.kodari.RepresentativeCoin.Retrofit.RptCoinSearchView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddInfo
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinAddResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResponse
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResult
import com.bit.kodari.RepresentativeCoin.Service.RptCoinService
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentRepresentativeCoinSearchBinding
import kotlin.properties.Delegates

class RepresentativeCoinSearchFragment : BaseFragment<FragmentRepresentativeCoinSearchBinding>(
    FragmentRepresentativeCoinSearchBinding::inflate), RptCoinSearchView, RptCoinAddView {

    private var coinList = ArrayList<RptCoinSearchResult>()
    private var filteredList  = ArrayList<RptCoinSearchResult>()
    private lateinit var rptCoinSearchRVAdapter: RptCoinSearchRVAdapter
    var coinIdx by Delegates.notNull<Int>()

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
                coinList[item.coinIdx-1].isCheck =true
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment().apply {
//                        arguments = Bundle().apply {
//                            putInt("coinIdx", item.coinIdx)
//                            putString("coinImage",item.coinImg)
//                            putString("coinName", item.coinName)
//                            putString("coinSymbol", item.symbol)
//                        }
//                    }).commitAllowingStateLoss()
            }
        })
        binding.representativeCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinSearchCoinListRV.adapter= rptCoinSearchRVAdapter
    }

    fun setListeners()
    {
        binding.representativeCoinSearchCompleteTV.setOnClickListener {
            // 대표 코인 추가 API
            var portIdx = 25 // 포트폴리오 인덱스 연결 필요
            val rptCoinAddInfo=RptCoinAddInfo(portIdx, coinIdx)
            Log.d("rptCoinAdd", "대표 코인 추가 정보 : ${portIdx}, ${coinIdx}")
            val rptCoinService=RptCoinService()
            rptCoinService.setRptCoinAddView(this)
            rptCoinService.getRptCoinAdd(rptCoinAddInfo)
        }

        binding.representativeCoinSearchSearchInputET.addTextChangedListener(object: TextWatcher {
            // 검색 기능
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

    // 검색용 코인 받아오기 성공
    override fun getCoinsAllSuccess(response: RptCoinSearchResponse) {
        coinList=response.result as ArrayList<RptCoinSearchResult>
        Log.d("getCoinsAllSuccess성공", "${coinList.size}")

        setRecyclerView()
    }

    // 검색용 코인 받아오기 실패
    override fun getCoinsAllFailure(message: String) {
        Log.d("getCoinsAllFailure", "코인 목록 불러오기 실패, ${message}")
    }

    // 대표 코인 추가 성공
    override fun rptCoinAddAllSuccess(response: RptCoinAddResponse) {
        when(response.code){
            1000 -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
            }
            else ->{
                Toast.makeText(context, "대표코인 추가 실패, &{response.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 대표 코인 추가 실패
    override fun rptCoinAddAllFailure(message: String) {
        Log.d("rptCoinAddFail", "$message")
    }
}