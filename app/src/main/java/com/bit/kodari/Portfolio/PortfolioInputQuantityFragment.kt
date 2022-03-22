package com.bit.kodari.Portfolio

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPortfolioInputQuantityBinding
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates


class PortfolioInputQuantityFragment(val marketIdx: Int) : BaseFragment<FragmentPortfolioInputQuantityBinding>(FragmentPortfolioInputQuantityBinding::inflate) {

    private lateinit var coinImg:String
    private lateinit var coinSymbol:String
    private lateinit var coinName:String
    private var coinIdx by Delegates.notNull<Int>()
    private lateinit var priceAvg:String
    private lateinit var amount:String

    private lateinit var callback:OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , PortfolioSearchFragment(marketIdx))
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        setInit()
        setListener()
    }

    fun setInit(){
        //코인 관련 정보
        if(requireArguments().containsKey("coinImage")){
            coinImg = requireArguments().getString("coinImage")!!
            Glide.with(binding.inputCoinImageIv)
                .load(coinImg)
                .error(R.drawable.white_radius)
                .into(binding.inputCoinImageIv)
        }

        if(requireArguments().containsKey("coinSymbol")){
            coinSymbol = requireArguments().getString("coinSymbol")!!
            binding.inputSymbolNameTv.text = coinSymbol
        }

        if(requireArguments().containsKey("coinName")){
            coinName = requireArguments().getString("coinName")!!
            binding.inputQuantityCoinNameTv.text = coinName
        }

        if(requireArguments().containsKey("coinIdx")){
            coinIdx = requireArguments().getInt("coinIdx")
        }
        //거래소 관련 정보
        if(marketIdx == 2){
            binding.inputUpbitLogoTv.text = "빗썸"
            binding.inputUpbitLogoIv.setImageResource(R.drawable.bithumb)
        }

    }

    fun setListener(){
        //완료 버튼 누르면 객체 생성해서 ManageMent로 넘겨줘야함.
        binding.inputQuantityCompleteTv.setOnClickListener {
            showToast("완료")
            priceAvg = binding.inputUnitPriceInputEt.text.toString()
            amount = binding.inputCountInputEt.text.toString()
            val coinDataResponse = CoinDataResponse(coinIdx,coinImg,coinName,coinSymbol, getUserIdx(),0,priceAvg,amount)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl,PortfolioManagementFragment(marketIdx).apply {
                    arguments = Bundle().apply {
                        //괄호 안의 Key 를 입력 하고 Object 타입으로 넘어오기 때문에 캐스팅을 통해서 데이터를 받으면 되겠습니다 .
                        //Serializable로 셋팅
                        putSerializable("coinDataResponse", coinDataResponse)
                    }
                }).commitAllowingStateLoss()
        }
        binding.inputQuantityPreviewBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl , PortfolioManagementFragment(marketIdx).apply{
                    arguments = Bundle().apply{

                    }
                })
                .commitAllowingStateLoss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}