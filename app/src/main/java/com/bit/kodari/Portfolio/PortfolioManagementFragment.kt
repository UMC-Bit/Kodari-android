package com.bit.kodari.Portfolio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Portfolio.Adapter.ManagementRVAdapter
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.Portfolio.Data.PostAccountRequest
import com.bit.kodari.Portfolio.Retrofit.PortManagementView
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPortfolioManagementBinding
//뒤로가기 눌렀을때 Home으로 가게 구현해야함 , 또 기존 정보 다 날라가게 해야함..
class PortfolioManagementFragment : BaseFragment<FragmentPortfolioManagementBinding>(FragmentPortfolioManagementBinding::inflate) , PortManagementView{

    lateinit var managementRVAdapter: ManagementRVAdapter       //일단 .. 데이터를 가져와보자.
    companion object{   //static으로 다시 돌아와도 리스트 초기화 되지 않게함.
        private var psnCoinList = ArrayList<CoinDataResponse>()     //가져온 코인을 추가하는 식으로 해야할 것 같음.
        private var cnt = 0;
    }


    override fun initAfterBinding() {
        setInit()
        setRecyclerView()                   //리싸이클러뷰 우선 셋팅
        setListener()
    }
    //문제점 1 : list가 계속 새로 선언되나 ?
    fun setInit(){
        //argument 자체가 없음.
        //만약 코인 객체가 넘어왔다면 psnCoinList에 셋팅
        //sharedPreference에 넣어야할듯
        if(requireArguments().isEmpty){     //홈 화면에서 넘어왔을 경우
            psnCoinList.clear()
            return
        }
        if(requireArguments().containsKey("coinDataResponse")){
            val coinDataResponse = requireArguments().getSerializable("coinDataResponse") as CoinDataResponse
            Log.d("coinData" , "$coinDataResponse")
            //managementRVAdapter.add(coinDataResponse)           //리스트에 추가하기.
            psnCoinList.add(coinDataResponse)
        }

    }

    fun setListener(){
        binding.portfolioManagementCoinBackBtnIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , EnrollExchangeFragment()).addToBackStack(null).commit()
        }

        binding.portfolioManagementSearchCoinBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , PortfolioSearchFragment()).addToBackStack(null).commit()
        }

        binding.portfolioManagementNextTextTv.setOnClickListener {
            //계좌생성 , 포토폴리오 생성 ,소유코인 생성 모두 해줘야함. 그 후 Home으로 돌아가게해야함.
            //Home에서 내 포토폴리오 있으면 데이터들 나오게 해야함.
            val accountName = binding.portfolioManagementInputAccountEt.text.toString()
            val property = binding.portfolioManagementInputAssetEt.text.toString()
            val postAccountRequest = PostAccountRequest(accountName,"1",property,getUserIdx())
            val addCoinList = ArrayList<PsnCoinAddInfo>()           //추가해야할 소유코인 목록들을 가지고 있는 배열
            for( cur in psnCoinList){
                var addCoin = PsnCoinAddInfo(getUserIdx(), cur.coinIdx,0,cur.priceAvg ,cur.amount)  //accountIdx는 다시 추가해야함
                addCoinList.add(addCoin)
            }
            Log.d("버튼 클릭" , "${psnCoinList.size}")
            val portfolioService = PortfolioService()
            portfolioService.setPortManagementView(this)
            showLoadingDialog(requireContext())
            portfolioService.postAccount(postAccountRequest,addCoinList)
        }
    }

    fun setRecyclerView(){
        managementRVAdapter = ManagementRVAdapter(psnCoinList)
        binding.portfolioManagementCoinListRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.portfolioManagementCoinListRv.adapter = managementRVAdapter
    }


    override fun makePortSuccess(response: PsnCoinAddResponse) {
        //생성 완료 됐을 때
        cnt++
        if(cnt == psnCoinList.size){            //넣은 코인만큼 호출되면 Home으로 돌아가자.
            showToast("생성 완료") // 다음 누르면 여기서 변경하게 하면 안될 것같은데 .. 이 함수가 몇번 이상 호출되면 실행되게 해야하지 않을까 ?
            dismissLoadingDialog()
            Log.d("makePortSuccess" ,"성공")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , HomeFragment()).commitAllowingStateLoss()
            //홈 화면으로 되돌리기
        }

    }

    override fun makePortFailure(message: String) {
        //생성 실패 했을 때
        dismissLoadingDialog()
        showToast(message)
    }

    override fun onDestroyView() {
        //여기서 coinList 초기화하면 안됨 -> 그러면 선택하러 넘어만가도 초기화 됨.
        super.onDestroyView()
        cnt = 0
    }

}