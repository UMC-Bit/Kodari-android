package com.bit.kodari.Debate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Adapter.DebateCoinRVAdapter
import com.bit.kodari.Debate.Data.DebateCoinResponse
import com.bit.kodari.Debate.Data.DebateCoinResult
import com.bit.kodari.Debate.Retrofit.DebateCoinView
import com.bit.kodari.Debate.Service.DebateService
import com.bit.kodari.databinding.DialogCoinBinding


class DialogCoin : DialogFragment(), DebateCoinView {

    private var _binding : DialogCoinBinding? = null
    private val binding get() = _binding!!
    private var coinList = ArrayList<DebateCoinResult>()
    private var filteredList  = ArrayList<DebateCoinResult>()
    private lateinit var debateCoinRVAdapter: DebateCoinRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCoinBinding.inflate(inflater,container,false)
        setListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getCoins()
    }


    fun setRecyclerView(){
        debateCoinRVAdapter = DebateCoinRVAdapter(coinList)
        binding.dialogListRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.dialogListRv.adapter = debateCoinRVAdapter
    }

    fun getCoins(){
        val debateService = DebateService()
        debateService.setDebateCoinView(this)
        debateService.getCoinsAll()
    }

    override fun getCoinsAllSuccess(response: DebateCoinResponse) {
        coinList = response.result as ArrayList<DebateCoinResult>
        Log.d("getCoinsAllSuccess" ,"${coinList.size}")
        setRecyclerView()
    }

    override fun getCoinsAllFailure(message: String) {
        Log.d("DebateCoin", "코인 목록 불러오기 실패 ,${message}")
    }

    fun setListener(){
        binding.dialogInputEt.doAfterTextChanged {
            var searchText =  binding.dialogInputEt.text.toString()
            searchFilter(searchText)
        }
    }

    fun searchFilter(searchText: String) {
        filteredList.clear()
        for (i in 0 until coinList.size) {
            if (coinList[i].coinName.toLowerCase()
                    .contains(searchText.toLowerCase())
            ) {
                filteredList.add(coinList[i])
            }
        }
        debateCoinRVAdapter.filterList(filteredList)
    }

    //다이얼로그 크기는 onResume에서 window이용해서 선언
    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
    }


    //메모리 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}