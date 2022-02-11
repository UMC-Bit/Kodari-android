package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.PossessionCoin.Adapter.MemoRVAdapter
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResult
import com.bit.kodari.databinding.FragmentEditPwBinding
import com.bit.kodari.databinding.FragmentMemoBinding

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {

    //더미데이터로 사용할 리스트
    val memoList = ArrayList<PsnCoinGetTradeResult>()

    override fun initAfterBinding() {
        //더미데이터 메모 하나만 넣어두기.
        setRecyclerView()
    }

    fun setRecyclerView(){
        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
        memoList.add(PsnCoinGetTradeResult(2,"sell","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
        val memoRVAdapter = MemoRVAdapter(memoList)
        binding.memoDialogRV.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL,false)
        binding.memoDialogRV.adapter = memoRVAdapter
    }

}