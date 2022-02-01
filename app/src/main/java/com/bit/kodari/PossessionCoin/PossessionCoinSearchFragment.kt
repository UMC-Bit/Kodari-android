package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Debate.Adapter.DebateCoinRVAdapter
import com.bit.kodari.Debate.Data.DebateCoinResult
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
    private var filteredList  = ArrayList<PsnCoinSearchResult>()
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

        setListeners()

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

    fun setRecyclerView(){
        possessionCoinSearchAdapter = PossessionCoinSearchAdapter(coinList)
        //아이템 클릭 리스너를 현재 뷰에서 처리
        //Adapter에 있는 position값과 같이 HomeFragment로 넘어와서 자동 셋팅
        possessionCoinSearchAdapter.setMyItemClickListener(object :PossessionCoinSearchAdapter.MyItemClickListener{
            override fun onItemClick(item: PsnCoinSearchResult) {      //이 아이템 클릭시 작동하게해야함
//                Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinAddFragment()).commitAllowingStateLoss()
            }
        })

        binding.possessionCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinSearchCoinListRV.adapter= possessionCoinSearchAdapter
    }

    fun setListeners(){
        binding.possessionCoinSearchSearchInputET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                    Log.d("setlistener", "실행")
                    var searchText =  binding.possessionCoinSearchSearchInputET.text.toString()
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
        possessionCoinSearchAdapter.filterList(filteredList)
    }

    fun getCoins(){
        val psnCoinService = PsnCoinService()
        psnCoinService.setPsnCoinSearchView(this)
        psnCoinService.getCoinsAll()
    }

    override fun getCoinsAllSuccess(response: PsnCoinSearchResponse) {
        coinList=response.result as ArrayList<PsnCoinSearchResult>
        Log.d("getCoinsAllSuccess성공", "${coinList.size}")

        setRecyclerView()
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("getCoinsAllFailure", "코인 목록 불러오기 실패, ${message}")
    }
}