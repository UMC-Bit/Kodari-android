package com.bit.kodari.Main

import android.media.tv.TvContract
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.Data.PortIdxResponse
import com.bit.kodari.Main.Data.PortfolioResponse
import com.bit.kodari.Portfolio.Retrofit.PortfolioView
import com.bit.kodari.Portfolio.Service.PortfolioService
import com.bit.kodari.R
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.databinding.FragmentMyPortfolioBinding
import java.text.NumberFormat
import kotlin.properties.Delegates

//포토폴리오 Index로 포토폴리오 조회환 뒤 binding 처리 해줘야할듯
class MyPortfolioFragment(val portIdx: Int, val homeFragment: HomeFragment) : BaseFragment<FragmentMyPortfolioBinding>(FragmentMyPortfolioBinding::inflate) ,PortfolioView{  //portIdx로 계좌 조회 ?
    private var checkView = true
    var profit: Int = 0
    var property: Double = 0.0
    var accoutIdx by Delegates.notNull<Int>()

    override fun initAfterBinding() {
        callMyPort()
        setListener()
        homeFragment.setPortFolioView(this)
    }

    override fun onDestroyView() {
        checkView = false
        super.onDestroyView()
    }
    fun setListener(){
        //편집 버튼 누르면 다이얼로그 띄우기
        binding.myPortfolioModifyIv.setOnClickListener {
            val dialog = ModifyInfoDialog(accoutIdx , portIdx).apply {
                arguments = Bundle().apply {
                    putString("accountName", binding.myPortfolioAccountNameTv.text.toString())
                    putString("myAsset", "${formatPrice(property)}")
                }
            }
            dialog.show(requireActivity().supportFragmentManager,"ModifyDialog")
        }
    }
    //portIdx에 해당하는 포토폴리오 가져오기
    fun callMyPort(){
        val portfolioService = PortfolioService()
        portfolioService.setPortfolioView(this)
        portfolioService.getPortfolioInfo(portIdx)
    }

    override fun getPortIdxSuccess(resp: PortIdxResponse) {
        //사용안함
    }

    override fun getPortIdxFailure(message: String) {
        //사용안함
    }

    override fun portfolioSuccess(resp: PortfolioResponse) {
        if(checkView) {
            //숫자 형태로 나타내기
            binding.myPortfolioAssetTv.text = formatPrice(resp.result.property) + "원"
            property = resp.result.property
            binding.myPortfolioAssetTv.text = formatPrice(resp.result.property)
            Log.d("temp", "${resp.result.property}")
            binding.myPortfolioAccountNameTv.text = resp.result.accountName
            accoutIdx = resp.result.accountIdx
            //resp.result.marketName 마켓이름 저장
        }
    }

    override fun portfolioFailure(message: String) {
        binding.myPortfolioAccountNameTv.text = "정보를 불러오는데 실패했습니다."
    }

    fun getAccountProfit(profit: Double, sumBuyCoin: Double) {
        if (checkView) {
            this.profit = profit.toInt()
            val profitRate = (profit / sumBuyCoin) * 100 - 100
            binding.myPortfolioAssetTv.text = formatPrice(property.toInt() + profit) + "원"
            binding.myPortfolioPercentTv.text = formatPrice(profitRate) + "%"
        }
    }
}