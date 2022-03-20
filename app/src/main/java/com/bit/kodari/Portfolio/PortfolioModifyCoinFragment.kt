package com.bit.kodari.Portfolio

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPortfolioModifyBinding
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class PortfolioModifyCoinFragment(position: Int) :
    BaseFragment<FragmentPortfolioModifyBinding>(FragmentPortfolioModifyBinding::inflate) {
    private lateinit var coinImg: String
    private lateinit var coinSymbol: String
    private lateinit var coinName: String
    private lateinit var coinPrice: String
    private lateinit var coinAmount: String
    private var coinIdx by Delegates.notNull<Int>()
    private lateinit var priceAvg: String
    private lateinit var amount: String
    private var position = position
    private lateinit var callback: OnBackPressedCallback
    private lateinit var coinDataResponse: CoinDataResponse
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PortfolioSearchFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun initAfterBinding() {
        setInit()
        setListener()
    }

    fun setInit() {
        if (requireArguments().containsKey("coinModifyResponse")) {
            coinDataResponse =
                requireArguments().getSerializable("coinModifyResponse") as CoinDataResponse
            Log.d("coinModifyData", "$coinDataResponse")
        }
        coinImg = coinDataResponse.coinImg
        Glide.with(binding.modifyCoinImageIv)
            .load(coinImg)
            .error(R.drawable.white_radius)
            .into(binding.modifyCoinImageIv)
        coinSymbol = coinDataResponse.symbol
        binding.modifySymbolNameTv.text = coinSymbol
        coinName = coinDataResponse.coinName
        binding.modifyQuantityCoinNameTv.text = coinName
        coinIdx = coinDataResponse.coinIdx
        coinPrice = PortfolioManagementFragment.psnCoinList.get(position).priceAvg
        binding.modifyUnitPriceInputEt.setText(coinPrice)
        coinAmount = PortfolioManagementFragment.psnCoinList.get(position).amount
        binding.modifyCountInputEt.setText(coinAmount)
    }

    fun setListener() {
        // 완료 버튼 누르면 PortfolioManagementFragment.psnCoinList 수정 됨
        binding.modifyQuantityCompleteTv.setOnClickListener {
            coinDataResponse.priceAvg = binding.modifyUnitPriceInputEt.getText().toString()
            coinDataResponse.amount = binding.modifyCountInputEt.getText().toString()
            priceAvg = binding.modifyUnitPriceInputEt.text.toString()
            amount = binding.modifyCountInputEt.text.toString()
            PortfolioManagementFragment.psnCoinList.set(position, coinDataResponse)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PortfolioManagementFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("coinModifyResponse", "true")
                    }
                }).commitAllowingStateLoss()
        }
        binding.modifyQuantityPreviewBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PortfolioManagementFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("coinModifyResponse", "true")
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