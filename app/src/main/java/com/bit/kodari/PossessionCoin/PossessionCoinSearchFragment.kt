package com.bit.kodari.PossessionCoin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinSearchAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinSearchView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinSearchBinding


class PossessionCoinSearchFragment(val accountName:String , val marketIdx:Int) : Fragment(), PsnCoinSearchView {
    lateinit var binding:FragmentPossessionCoinSearchBinding
    private var coinList = ArrayList<PsnCoinSearchResult>()
    private var filteredList  = ArrayList<PsnCoinSearchResult>()
    private lateinit var possessionCoinSearchAdapter: PossessionCoinSearchAdapter
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName ,marketIdx)).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onStart() {
        super.onStart()
        setInit()
        getCoins()
    }

    //marketIdx ??? ?????? 2??? logo??? text ??????
    private fun setInit() {
        if(marketIdx == 2){
            binding.possessionCoinSearchExchangeLogoIV.setImageResource(R.drawable.bithumb)
            binding.possessionCoinSearchExchangeNameTV.text = "??????"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
            savedInstanceState: Bundle?
            ): View? {

            binding= FragmentPossessionCoinSearchBinding.inflate(inflater , container , false)

            setListeners()

            binding.possessionCoinSearchBackIV.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName ,marketIdx)).commit()
        }

        return binding.root
    }

    fun setRecyclerView(){
        possessionCoinSearchAdapter = PossessionCoinSearchAdapter(coinList)
        //????????? ?????? ???????????? ?????? ????????? ??????
        //Adapter??? ?????? position?????? ?????? HomeFragment??? ???????????? ?????? ??????
        possessionCoinSearchAdapter.setMyItemClickListener(object :PossessionCoinSearchAdapter.MyItemClickListener{
            override fun onItemClick(item: PsnCoinSearchResult) {      //??? ????????? ????????? ?????????????????????
//                Toast.makeText(requireContext(),"${item.coinName}" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinAddFragment(accountName ,marketIdx).apply {
                        arguments = Bundle().apply {
                            putInt("coinIdx", item.coinIdx)
                            putString("coinImage",item.coinImg)
                            putString("coinName", item.coinName)
                            putString("coinSymbol", item.symbol)
                        }
                    }).commit()
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
                    Log.d("setlistener", "??????")
                    var searchText =  binding.possessionCoinSearchSearchInputET.text.toString()
                    searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        Log.d("searchFilter", "??????")
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
        psnCoinService.getMarketCoin(marketIdx)
    }

    override fun getCoinsAllSuccess(response: PsnCoinSearchResponse) {
        coinList=response.result as ArrayList<PsnCoinSearchResult>
        Log.d("getCoinsAllSuccess??????", "${coinList.size}")

        setRecyclerView()
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("getCoinsAllFailure", "?????? ?????? ???????????? ??????, ${message}")
    }

    override fun getMarketCoinSuccess(response: PsnCoinSearchResponse) {
        coinList=response.result as ArrayList<PsnCoinSearchResult>
        Log.d("getCoinsAllSuccess??????", "${coinList.size}")

        setRecyclerView()
    }

    override fun getMarketCoinFailure(message: String) {
        Log.d("getCoinsAllFailure", "?????? ?????? ???????????? ??????, ${message}")
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}