package com.bit.kodari.Portfolio

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Portfolio.Adapter.SearchCoinRVAdapter
import com.bit.kodari.Portfolio.Retrofit.SearchCoinView
import com.bit.kodari.Portfolio.Data.SearchCoinResponse
import com.bit.kodari.Portfolio.Data.SearchCoinResult
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPortfolioSearchBinding

class PortfolioSearchFragment(val marketIdx:Int): BaseFragment<FragmentPortfolioSearchBinding>(
    FragmentPortfolioSearchBinding::inflate), SearchCoinView {

    private var coinList = ArrayList<SearchCoinResult>()
    private var filteredList  = ArrayList<SearchCoinResult>()
    private lateinit var searchCoinRVAdapter: SearchCoinRVAdapter
    private lateinit var callback : OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PortfolioManagementFragment(marketIdx).apply {
                        arguments = Bundle().apply {
                            putSerializable("coinSearchResponse", "true")
                        }
                    }).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        setInit()
        getMarketCoins()
        setListeners()

        binding.portfolioSearchBackBtnIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PortfolioManagementFragment(marketIdx).apply {
                    arguments = Bundle().apply {
                        putSerializable("coinSearchResponse", "true")
                    }
                }).commit()
        }
    }

    private fun setInit() {
        if(marketIdx == 2){     //2번으로 넘어왔을 경우
            binding.portfolioSearchUpbitLogoTv.text = "빗썸"
            binding.portfolioSearchUpbitLogoIv.setImageResource(R.drawable.bithumb)
        }
    }

    fun setRecyclerView(){
        searchCoinRVAdapter = SearchCoinRVAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        searchCoinRVAdapter.setMyItemClickListener(object : SearchCoinRVAdapter.MyItemClickListener{
            override fun onItemClick(item: SearchCoinResult) {      //이 아이템 클릭시 작동하게해야함
//                Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PortfolioInputQuantityFragment(marketIdx).apply {
                        arguments = Bundle().apply {
                            putString("coinImage",item.coinImg)
                            putString("coinName", item.coinName)
                            putString("coinSymbol", item.symbol)
                            putInt("coinIdx" , item.coinIdx)
                        }
                    }).addToBackStack(null).commitAllowingStateLoss()
            }
        })

        binding.portfolioSearchRv.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.portfolioSearchRv.adapter= searchCoinRVAdapter
    }

    fun setListeners(){
        binding.portfolioSearchInputEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("setlistener", "실행")
                var searchText =  binding.portfolioSearchInputEt.text.toString()
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
        searchCoinRVAdapter.filterList(filteredList)
    }

    fun getCoins(){
        val portfolioService = PortfolioService()
        portfolioService.setSearchCoinView(this)
        showLoadingDialog(requireContext())
        portfolioService.getCoinsAll()
    }

    fun getMarketCoins(){
        val portfolioService = PortfolioService()
        portfolioService.setSearchCoinView(this)
        showLoadingDialog(requireContext())
        portfolioService.getMarketCoin(marketIdx)
    }

    override fun getSearchCoinAllSuccess(response: SearchCoinResponse) {
        dismissLoadingDialog()
        coinList=response.result as ArrayList<SearchCoinResult>
        Log.d("getSearchCoinAllSuccess", "${coinList.size}")

        setRecyclerView()
    }

    override fun getSearchCoinAllFailure(message: String) {
        dismissLoadingDialog()
        Log.d("getSearchCoinAllFailure", "코인 목록 불러오기 실패, ${message}")
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}
