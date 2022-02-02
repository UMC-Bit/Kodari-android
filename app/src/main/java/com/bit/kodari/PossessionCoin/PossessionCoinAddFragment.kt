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
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.LoginFragment
import com.bit.kodari.Login.Retrofit.SignUpView
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.Login.SignupPwFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddResponse
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinAddBinding
import com.bit.kodari.databinding.FragmentSignupNicknameBinding
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.util.*

class PossessionCoinAddFragment : BaseFragment<FragmentPossessionCoinAddBinding>(FragmentPossessionCoinAddBinding::inflate) , PsnCoinAddView {
    val tradeTime = StringBuilder()

    private lateinit var userIdx : String // 유저 인덱스
    private lateinit var coinIdx : String // 코인 인덱스
    private lateinit var accountIdx : String // 계좌 인덱스
    private lateinit var priceAvg : String // 매수 평단가 ET에서 가져오기
    private lateinit var amount : String // 수량 ET에서 가져오기


    override fun initAfterBinding() {
        moveLayout()
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
                binding.possessionCoinAddDayInputET.setText(tradeTime.toString())
                tradeTime.setLength(0)
            }, hour, minute, false)

        val datePicker = DatePickerDialog(context as MainActivity,
            { view, year, month, dayOfMonth ->
                val date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth)
                tradeTime.append(date)
                tradeTime.append("")
                timePicker.show()
            }, year, month, day)

        binding.possessionCoinAddDayInputET.setOnClickListener { v ->
            datePicker.show()
        }
    }

    fun moveLayout()
    {
        binding.possessionCoinAddCompleteButtonTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinAddBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment()).commitAllowingStateLoss()
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

            priceAvg=binding.possessionCoinAddAverageunitPriceInputET.text.toString()
            amount=binding.possessionCoinAddQuantityInputET.text.toString()
            val psnCoinAddinfo = PsnCoinAddInfo(userIdx,coinIdx, accountIdx, priceAvg, amount)
            Log.d("psnCoinAdd", "정보 : ${userIdx} , ${coinIdx} , ${accountIdx}, ${priceAvg}, ${amount}")
            val psnCoinService = PsnCoinService()
            psnCoinService.setPsnCoinAdd(this)
            psnCoinService.getPsnCoinAdd(psnCoinAddinfo)
        }

        binding.possessionCoinAddBeforeButtonIV.setOnClickListener{
            (context as LoginActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.login_container_fl, SignupPwFragment()).commitAllowingStateLoss()
        }
    }



    override fun psnCoinAddSuccess(response: PsnCoinAddResponse) {
        when(response.code){
            1000 -> {
                Toast.makeText(context,"소유코인 추가 성공" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.temp, PossessionCoinManagementFragment()).commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context,"소유코인 추가 실패 , ${response.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun psnCoinAddFailure(message: String) {
        showToast("소유코인 추가 실패 ,$message")
    }
}