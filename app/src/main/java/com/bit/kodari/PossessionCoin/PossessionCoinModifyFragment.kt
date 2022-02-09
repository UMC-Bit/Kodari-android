package com.bit.kodari.PossessionCoin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddTradeView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding
import com.bit.kodari.databinding.FragmentPossessionCoinModifyBinding
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.util.*
import kotlin.properties.Delegates

class PossessionCoinModifyFragment : Fragment(), PsnCoinAddTradeView {
    lateinit var binding:FragmentPossessionCoinModifyBinding
    val tradeTime = StringBuilder()
    var coinIdx by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinModifyBinding.inflate(inflater, container, false)

        moveLayout()
        datetimepicker()
        BuyAndSellButton()
        getCoinInformation()

        return binding.root
    }

    fun datetimepicker()
    {
        val mcurrentTime = Calendar.getInstance()
        val year = mcurrentTime.get(Calendar.YEAR)
        val month = mcurrentTime.get(Calendar.MONTH)
        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

        val mcurrent= Calendar.getInstance()
        val hour = mcurrent.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrent.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(context as MainActivity,
            { view, hourOfDay, minute ->
                val date = String.format("%02d:%02d:00", hourOfDay, minute)
                tradeTime.append(date);
                binding.possessionCoinModifyDateInputET.setText(tradeTime.toString())
                tradeTime.setLength(0)
            }, hour, minute, false)

        val datePicker = DatePickerDialog(context as MainActivity,
            { view, year, month, dayOfMonth ->
                val date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth)
                tradeTime.append(date)
                tradeTime.append("")
                timePicker.show()
            }, year, month, day)

        binding.possessionCoinModifyDateInputET.setOnClickListener { v ->
            datePicker.show()
        }
    }

    fun BuyAndSellButton()
    {
        binding.possessionCoinModifyBuyOffTV.setOnClickListener {
            binding.possessionCoinModifyBuyOffTV.visibility=View.GONE
            binding.possessionCoinModifyBuyOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifySellOnTV.visibility=View.GONE
            binding.possessionCoinModifySellOffTV.visibility=View.VISIBLE
        }

        binding.possessionCoinModifySellOffTV.setOnClickListener {
            binding.possessionCoinModifySellOffTV.visibility=View.GONE
            binding.possessionCoinModifySellOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifyBuyOnTV.visibility=View.GONE
            binding.possessionCoinModifyBuyOffTV.visibility=View.VISIBLE
        }
    }

    fun moveLayout()
    {
        binding.possessionCoinModifyCompleteButtonTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinModifyBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }
    }

    // 소유 코인 관리 fragement에서 선택한 코인 정보(코인 이름, 코인 심볼, 코인 이미지)를 수정 fragment로 가져오기
    fun getCoinInformation()
    {
        val imageView: ImageView =binding.possessionCoinModifyCoinImageIV

        if(requireArguments().containsKey("coinIdx")){
            coinIdx=requireArguments().getInt("coinIdx")
        }

        if(requireArguments().containsKey("coinImage")){
            Glide.with(imageView).load(requireArguments().getString("coinImage"))
                .into(imageView)
        }

        if(requireArguments().containsKey("coinName")){
            binding.possessionCoinModifyCoinNameTV.text=requireArguments().getString("coinName")
        }

        if(requireArguments().containsKey("coinSymbol")){
            binding.possessionCoinModifyCoinSymbolTV.text=requireArguments().getString("coinSymbol")
        }

        if(requireArguments().containsKey("amount")){
            binding.possessionCoinModifyQuantityNumberTV.text=requireArguments().getString("amount")
        }

        if(requireArguments().containsKey("priceAvg")){
            binding.possessionCoinModifyAverageunitPriceNumberTV.text=requireArguments().getString("priceAvg")
        }
    }

    fun setListener()
    {
        binding.possessionCoinModifyCompleteButtonTV.setOnClickListener {
            val psnCoinService = PsnCoinService()
            // 거래 내역 생성 API
            var portIdx=25
            var feeText = binding.possessionCoinModifyFeeInputET.text.toString()
            var fee: Double = 0.05
            if(feeText.isNotEmpty())
                fee = feeText.toDouble()
            var priceAvg = binding.possessionCoinModifyAverageunitPriceTV.text.toString()
            var amount = binding.possessionCoinModifyQuantityInputET.text.toString()
            var category = "buy"
            var memo=binding.possessionCoinModifyMemoInputET.text.toString()
            var date=binding.possessionCoinModifyDateInputET.text.toString()
            val psnCoinAddTradeInfo = PsnCoinAddTradeInfo(
                portIdx, coinIdx, priceAvg,
                amount, fee, category,
                memo, date
            )
            Log.d(
                "psnCoinTradeAdd",
                "거래 내역 정보 : ${portIdx}, ${coinIdx}, ${priceAvg}, " +
                        "${psnCoinAddTradeInfo.amount}, ${psnCoinAddTradeInfo.fee}, ${psnCoinAddTradeInfo.category}, ${psnCoinAddTradeInfo.memo}, ${psnCoinAddTradeInfo.date}"
            )
            psnCoinService.setPsnCoinAddTradeView(this)
            psnCoinService.getPsnCoinAddTrade(psnCoinAddTradeInfo)
        }
    }

    override fun psnCoinAddTradeSuccess(response: PsnCoinAddTradeResponse) {
        when(response.code){
            1000 -> {
                Toast.makeText(context,"거래내역 추가 성공" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context,"거래내역 추가 실패 , ${response.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun psnCoinAddTradeFailure(message: String) {
        Log.d("failaddtrade" ,"$message")
    }
}