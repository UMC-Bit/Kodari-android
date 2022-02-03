package com.bit.kodari.PossessionCoin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.SignupPwFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddTradeView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddTradeResponse
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinAddBinding
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.util.*

class PossessionCoinAddFragment : BaseFragment<FragmentPossessionCoinAddBinding>(FragmentPossessionCoinAddBinding::inflate) , PsnCoinAddView,
    PsnCoinAddTradeView {
    val tradeTime = StringBuilder()


    inner class psnCoinAddInfo()
    {
        lateinit var userIdx : String // 유저 인덱스
        lateinit var coinIdx : String // 코인 인덱스
        lateinit var accountIdx : String // 계좌 인덱스
        lateinit var priceAvg : String // 매수 평단가 ET
        lateinit var amount : String // 수량 ET
    }

    inner class psnCoinAddTradeInfo()
    {
        lateinit var portIdx : String // 포트폴리오 인덱스
        lateinit var coinIdx : String // 코인 인덱스
        lateinit var price : String // 매수 평단가 ET
        lateinit var amount : String // 수량 ET
        lateinit var fee : String // 수수료 ET
        lateinit var category : String // 카테고리(Buy&Sell)
        lateinit var memo : String // 메모 ET
        lateinit var date: String // 날짜 및 시간 ET
    }

    override fun initAfterBinding() {

        binding.possessionCoinAddBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment())
                .commitAllowingStateLoss()
        }
        datetimepicker()
        getCoinInformation()
        setListener()
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
                binding.possessionCoinAddDateInputET.setText(tradeTime.toString())
                tradeTime.setLength(0)
            }, hour, minute, false)

        val datePicker = DatePickerDialog(context as MainActivity,
            { view, year, month, dayOfMonth ->
                val date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth)
                tradeTime.append(date)
                tradeTime.append("")
                timePicker.show()
            }, year, month, day)

        binding.possessionCoinAddDateInputET.setOnClickListener { v ->
            datePicker.show()
        }
    }

    // 전체 코인 검색 창에서 선택한 코인 정보(코인 이름, 코인 심볼, 코인 이미지) 가져오기
    fun getCoinInformation()
    {
        val imageView: ImageView=binding.possessionCoinAddCoinImageIV

        if(requireArguments().containsKey("coinImage")){
            Glide.with(imageView).load(requireArguments().getString("coinImage"))
                .into(imageView)
        }

        if(requireArguments().containsKey("coinName")){
            binding.possessionCoinAddCoinNameTV.text=requireArguments().getString("coinName")
        }

        if(requireArguments().containsKey("coinSymbol")){
            binding.possessionCoinAddCoinSymbolTV.text=requireArguments().getString("coinSymbol")
        }
    }

    fun setListener()
    {
        binding.possessionCoinAddCompleteButtonTV.setOnClickListener {
            // 소유 코인 추가 API
            psnCoinAddInfo().priceAvg =
                binding.possessionCoinAddAverageunitPriceInputET.text.toString()
            psnCoinAddInfo().amount = binding.possessionCoinAddQuantityInputET.text.toString()
            val psnCoinAddinfo = PsnCoinAddInfo(
                psnCoinAddInfo().userIdx, psnCoinAddInfo().coinIdx, psnCoinAddInfo().accountIdx,
                psnCoinAddInfo().priceAvg, psnCoinAddInfo().amount
            )
            Log.d(
                "psnCoinAdd",
                "소유 코인 정보 : ${psnCoinAddInfo().userIdx} , ${psnCoinAddInfo().coinIdx} , " +
                        "${psnCoinAddInfo().accountIdx}, ${psnCoinAddInfo().priceAvg}, ${psnCoinAddInfo().amount}"
            )
            val psnCoinService = PsnCoinService()
            psnCoinService.setPsnCoinAddView(this)
            psnCoinService.getPsnCoinAdd(psnCoinAddinfo)

            // 거래 내역 생성 API
            psnCoinAddTradeInfo().price =
                binding.possessionCoinAddAverageunitPriceInputET.text.toString()
            psnCoinAddTradeInfo().amount = binding.possessionCoinAddQuantityInputET.text.toString()
            psnCoinAddTradeInfo().fee = binding.possessionCoinAddFeeInputET.text.toString()
            psnCoinAddTradeInfo().memo = binding.possessionCoinAddMemoInputET.text.toString()
            psnCoinAddTradeInfo().date = binding.possessionCoinAddDateInputET.text.toString()
            val psnCoinAddTradeInfo = PsnCoinAddTradeInfo(
                psnCoinAddTradeInfo().portIdx,
                psnCoinAddTradeInfo().coinIdx,
                psnCoinAddTradeInfo().price,
                psnCoinAddTradeInfo().amount,
                psnCoinAddTradeInfo().fee,
                psnCoinAddTradeInfo().category,
                psnCoinAddTradeInfo().memo,
                psnCoinAddTradeInfo().date
            )
            Log.d(
                "psnCoinTradeAdd",
                "거래 내역 정보 : ${psnCoinAddTradeInfo().portIdx}, ${psnCoinAddTradeInfo().coinIdx}, ${psnCoinAddTradeInfo().price}, " +
                        "${psnCoinAddTradeInfo().amount}, ${psnCoinAddTradeInfo().fee}, ${psnCoinAddTradeInfo().category}, ${psnCoinAddTradeInfo().memo}, ${psnCoinAddTradeInfo().date}"
            )
            psnCoinService.setPsnCoinAddTradeView(this)
            psnCoinService.getPsnCoinAddTrade(psnCoinAddTradeInfo)

        }
    }



    override fun psnCoinAddSuccess(response: PsnCoinAddResponse) {
        when(response.code){
            1000 -> {
                Toast.makeText(context,"소유코인 추가 성공" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context,"소유코인 추가 실패 , ${response.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun psnCoinAddFailure(message: String) {
        Log.d("failadd" ,"$message")
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