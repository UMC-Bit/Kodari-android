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
import com.bit.kodari.PossessionCoin.RetrofitData.DeleteTradeResponse
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
        val memoRVAdapter = MemoRVAdapter(memoList)
        memoRVAdapter.setMyItemClickListener(object : MemoRVAdapter.MemoClickListener{
            override fun onDeleteClick(item: GetTradeListResult) {
                callDeleteTrade(item.tradeIdx)      //클릭시 삭제 호출
                Log.d("tradeIdx" , "${item.tradeIdx}")
            }
        })
        binding.memoDialogRV.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL,false)
        binding.memoDialogRV.adapter = memoRVAdapter
    }

    private fun callTradeList(coinIdx: Int){
        Log.d("삭제 후 실행" , "callTradeList")
        val homeService = HomeService()
        homeService.setMemoView(this)
        showLoadingDialog(requireContext())
        homeService.getTradeList(coinIdx)
    }

    private fun callDeleteTrade(tradeIdx:Int){
        val homeService = HomeService()
        homeService.setMemoView(this)
        showLoadingDialog(requireContext())
        homeService.deleteTrade(tradeIdx)
    }


    //거래내역 불러오기 성공했을떄
    override fun getTradeListSuccess(response: GetTradeListResponse) {
        dismissLoadingDialog()
        memoList = response.result
        memoList[0].isFirst = true                      //가장 최근것만 삭제 가능하게 구분.
        Log.d("getTrade", "${memoList.size}")
        setRecyclerView()
    }
    //입력값을 입력해주세요 -> 뜨면서 삭제는 됨


    //거래내역 불러오기 실패했을떄
    override fun getTradeListFailure(message: String) {
        dismissLoadingDialog()
        Log.d("getTradeList" , "실패")
        showToast(message)
    }

    //거래내역 삭제 성공했을때
    override fun deleteTradeSuccess(response: DeleteTradeResponse) {
        dismissLoadingDialog()
        callTradeList(coinIdx)      //삭제 후 재조회
    }

    //거래내역 삭제 실패했을떄
    override fun deleteTradeFailure(message: String) {
        dismissLoadingDialog()
        showToast(message)
    }
}