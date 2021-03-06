package com.bit.kodari.PossessionCoin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Main.HomeFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
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

class PossessionCoinModifyFragment(val accountName:String , val marketIdx:Int) : BaseFragment<FragmentPossessionCoinModifyBinding>(FragmentPossessionCoinModifyBinding::inflate), PsnCoinAddTradeView {
    private val tradeTime = StringBuilder()
    private var coinIdx by Delegates.notNull<Int>()

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName , marketIdx)).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        datetimepicker()
        setListener()

        getCoinInformation()
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

    // ?????? ?????? ?????? fragement?????? ????????? ?????? ??????(?????? ??????, ?????? ??????, ?????? ?????????)??? ?????? fragment??? ????????????
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
            Log.d("amout", "${requireArguments().getDouble("amount")}")
            binding.possessionCoinModifyQuantityNumberTV.text=requireArguments().getDouble("amount").toString()
        }

        if(requireArguments().containsKey("priceAvg")){
            val value = requireArguments().getDouble("priceAvg")!!
            binding.possessionCoinModifyAverageunitPriceNumberTV.text=String.format("%.2f", value)
        }


    }

    fun setListener()
    {
        binding.possessionCoinModifyCompleteButtonTV.setOnClickListener {
            // ?????? ?????? ?????? API
            val portIdx= MyApplicationClass.myPortIdx
            var fee = 0.05
            if(binding.possessionCoinModifyFeeInputET.text.toString().isNotEmpty()) fee = binding.possessionCoinModifyFeeInputET.text.toString().toDouble()
            val price = binding.possessionCoinModifyPriceInputET.text.toString()
            val amount = binding.possessionCoinModifyQuantityInputET.text.toString()
            var category = "buy"                                                    //???????????? ??????
            if(binding.possessionCoinModifySellOnTV.visibility == View.VISIBLE){ //????????? ????????????????????????
                category = "sell"
            }
            val memo=binding.possessionCoinModifyMemoInputET.text.toString()
            val date=binding.possessionCoinModifyDateInputET.text.toString()
            val psnCoinAddTradeInfo = PsnCoinAddTradeInfo(
                portIdx, coinIdx, price,
                amount, fee, category,
                memo, date
            )

            val psnCoinService = PsnCoinService()
            psnCoinService.setPsnCoinAddTradeView(this)
            showLoadingDialog(requireContext())
            psnCoinService.getPsnCoinAddTrade(psnCoinAddTradeInfo)
        }

        //?????? ????????????
        binding.possessionCoinModifyBuyOffTV.setOnClickListener {
            binding.possessionCoinModifyBuyOffTV.visibility=View.GONE
            binding.possessionCoinModifyBuyOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifySellOnTV.visibility=View.GONE
            binding.possessionCoinModifySellOffTV.visibility=View.VISIBLE
        }
        //?????? ????????????
        binding.possessionCoinModifySellOffTV.setOnClickListener {
            binding.possessionCoinModifySellOffTV.visibility=View.GONE
            binding.possessionCoinModifySellOnTV.visibility=View.VISIBLE
            binding.possessionCoinModifyBuyOnTV.visibility=View.GONE
            binding.possessionCoinModifyBuyOffTV.visibility=View.VISIBLE
        }

        //??????????????? ????????????
        binding.possessionCoinModifyBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName ,marketIdx)).commit()
        }
    }

    override fun psnCoinAddTradeSuccess(response: PsnCoinAddTradeResponse) {
        dismissLoadingDialog()
        when(response.code){
            1000 -> {
                Toast.makeText(context,"???????????? ?????? ??????" , Toast.LENGTH_SHORT).show()
                PossessionCoinManagementAdapter.isClick = false
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName ,marketIdx)).commit()

            }
            else -> {
                Toast.makeText(context,"???????????? ?????? ?????? , ${response.message}" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun psnCoinAddTradeFailure(message: String) {
        dismissLoadingDialog()
        Log.d("failaddtrade" ,"$message")
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}