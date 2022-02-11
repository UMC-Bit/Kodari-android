package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Data.GetTradeListResponse
import com.bit.kodari.Main.Data.GetTradeListResult
import com.bit.kodari.Main.RetrofitInterface.MemoView
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.PossessionCoin.Adapter.MemoRVAdapter
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResult
import com.bit.kodari.databinding.FragmentEditPwBinding
import com.bit.kodari.databinding.FragmentMemoBinding

class MemoFragment(val coinIdx:Int) : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) , MemoView {

    //더미데이터로 사용할 리스트
    var memoList = ArrayList<GetTradeListResult>()

    override fun initAfterBinding() {
        //더미데이터 메모 하나만 넣어두기.
        callTradeList(coinIdx)
    }
    //여기서 해당 소유코인에 대한 값들 불러와야함.
    fun setRecyclerView(){
//        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
//        memoList.add(PsnCoinGetTradeResult(2,"sell","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
//        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
//        memoList.add(PsnCoinGetTradeResult(2,"buy","BTC","2022-01-01",27.5,"테스트",10000,"active",31))
        val memoRVAdapter = MemoRVAdapter(memoList)
        binding.memoDialogRV.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL,false)
        binding.memoDialogRV.adapter = memoRVAdapter
    }

    fun callTradeList(coinIdx: Int){
        val homeService = HomeService()
        homeService.setMemoView(this)
        showLoadingDialog(requireContext())
        homeService.getTradeList(coinIdx)
    }

    //거래내역 불러오기 성공했을떄
    override fun getTradeListSuccess(response: GetTradeListResponse) {
        dismissLoadingDialog()
        memoList = response.result
        Log.d("getTrade", "${memoList}")
        setRecyclerView()
    }


    //거래내역 불러오기 실패했을떄
    override fun getTradeListFailure(message: String) {
        dismissLoadingDialog()
        showToast(message)
    }
}