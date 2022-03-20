package com.bit.kodari.Portfolio

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Portfolio.Adapter.ManagementRVAdapter
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.Portfolio.Data.PostAccountRequest
import com.bit.kodari.Portfolio.Retrofit.PortManagementView
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPortfolioManagementBinding
import java.time.LocalDate
import java.time.LocalDateTime

//뒤로가기 눌렀을때 Home으로 가게 구현해야함 , 또 기존 정보 다 날라가게 해야함..
class PortfolioManagementFragment :
    BaseFragment<FragmentPortfolioManagementBinding>(FragmentPortfolioManagementBinding::inflate),
    PortManagementView {

    private lateinit var managementRVAdapter: ManagementRVAdapter       //일단 .. 데이터를 가져와보자.
    private lateinit var callback:OnBackPressedCallback

    companion object {   //static으로 다시 돌아와도 리스트 초기화 되지 않게함.
        var psnCoinList = ArrayList<CoinDataResponse>()     //가져온 코인을 추가하는 식으로 해야할 것 같음.
        private var cnt = 0;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, EnrollExchangeFragment()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAfterBinding() {
        setInit()
        setRecyclerView()                   //리싸이클러뷰 우선 셋팅
        setListener()
    }

    //문제점 1 : list가 계속 새로 선언되나 ?
    fun setInit() {
        //argument 자체가 없음.
        //만약 코인 객체가 넘어왔다면 psnCoinList에 셋팅
        //sharedPreference에 넣어야할듯
        if (requireArguments().isEmpty) {     //홈 화면에서 넘어왔을 경우
            psnCoinList.clear()
            return
        } else if (requireArguments().containsKey("coinDataResponse")) {
            val coinDataResponse =
                requireArguments().getSerializable("coinDataResponse") as CoinDataResponse
            Log.d("coinData", "$coinDataResponse")
            //managementRVAdapter.add(coinDataResponse)           //리스트에 추가하기.
            psnCoinList.add(coinDataResponse)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setListener() {
        binding.root.setOnTouchListener { view, motionEvent ->
            hideKeyboard()
            false
        }

        binding.portfolioManagementCoinBackBtnIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, EnrollExchangeFragment()).commit()
        }

        binding.portfolioManagementSearchCoinBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PortfolioSearchFragment()).commit()
        }

        binding.portfolioManagementNextTextTv.setOnClickListener {
            //계좌생성 , 포토폴리오 생성 ,소유코인 생성 모두 해줘야함. 그 후 Home으로 돌아가게해야함.
            //Home에서 내 포토폴리오 있으면 데이터들 나오게 해야함.
            val accountName = binding.portfolioManagementInputAccountEt.text.toString()
            val property = binding.portfolioManagementInputAssetEt.text.toString()
            val postAccountRequest = PostAccountRequest(accountName, "1", property, getUserIdx())
            //val addCoinList = ArrayList<PsnCoinAddTradeInfo>()           //추가해야할 소유코인 목록들을 가지고 있는 배열
            val addCoinList = ArrayList<PsnCoinAddTradeInfo>()
            for (cur in psnCoinList) {       //여기서 거래 생성으로 바꿔야함
                //LocalDateTime 이 현재시간 가져오는것
                //val addCoin = PsnCoinAddInfo(getUserIdx(),cur.coinIdx,MyApplicationClass.myAccountIdx,cur.priceAvg,cur.amount)
                val addCoin = PsnCoinAddTradeInfo(
                    0, cur.coinIdx, cur.priceAvg, cur.amount, 0.05, "buy", "첫 등록",
                    LocalDateTime.now().toString()
                )
                //var addCoin = PsnCoinAddInfo(getUserIdx(), cur.coinIdx,0,cur.priceAvg ,cur.amount)  //accountIdx는 다시 추가해야함
                addCoinList.add(addCoin)
            }
            Log.d("버튼 클릭", "${psnCoinList.size}")
            val portfolioService = PortfolioService()
            portfolioService.setPortManagementView(this)
            showLoadingDialog(requireContext())
            portfolioService.postAccount(postAccountRequest, addCoinList)
        }
    }

    fun setRecyclerView() {
        managementRVAdapter = ManagementRVAdapter(this, psnCoinList)
        binding.portfolioManagementCoinListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.portfolioManagementCoinListRv.adapter = managementRVAdapter
    }

    override fun makePortSuccess(response: PsnCoinAddTradeResponse) {
        //생성 완료 됐을 때
        cnt++
        if (cnt == psnCoinList.size) {            //넣은 코인만큼 호출되면 Home으로 돌아가자.
            showToast("포트폴리오 생성 완료") // 다음 누르면 여기서 변경하게 하면 안될 것같은데 .. 이 함수가 몇번 이상 호출되면 실행되게 해야하지 않을까 ?
            dismissLoadingDialog()
            Log.d("makePortSuccess", "성공")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, HomeFragment()).commit()
            //홈 화면으로 되돌리기
        }
    }

    //다른 곳 클릭시 올라온 키보드 내려가게함
    fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun makePortFailure(message: String) {
        //생성 실패 했을 때
        dismissLoadingDialog()
        showToast(message)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onDestroyView() {
        //여기서 coinList 초기화하면 안됨 -> 그러면 선택하러 넘어만가도 초기화 됨.
        super.onDestroyView()
        cnt = 0
    }
}