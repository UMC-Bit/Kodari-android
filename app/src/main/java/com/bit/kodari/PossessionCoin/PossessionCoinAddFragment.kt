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
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinSearchAdapter
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddTradeView
import com.bit.kodari.PossessionCoin.Retrofit.PsnCoinAddView
import com.bit.kodari.PossessionCoin.RetrofitData.*
import com.bit.kodari.PossessionCoin.Service.PsnCoinService
import com.bit.kodari.R
import com.bit.kodari.Util.getUserIdx
import com.bit.kodari.databinding.FragmentPossessionCoinAddBinding
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.util.*
import kotlin.properties.Delegates
class PossessionCoinAddFragment : BaseFragment<FragmentPossessionCoinAddBinding>(FragmentPossessionCoinAddBinding::inflate) , PsnCoinAddView,
    PsnCoinAddTradeView {
    val tradeTime = StringBuilder()
    var coinIdx by Delegates.notNull<Int>()

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
                tradeTime.append(date)
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
        if(requireArguments().containsKey("coinIdx")){
            coinIdx=requireArguments().getInt("coinIdx")
        }
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
            var userIdx = getUserIdx()
            var accountIdx=32
            var priceAvg = binding.possessionCoinAddAverageunitPriceInputET.text.toString()
            var amount = binding.possessionCoinAddQuantityInputET.text.toString()
            val psnCoinAddinfo = PsnCoinAddInfo(userIdx, coinIdx, accountIdx, priceAvg, amount)
            Log.d(
                "psnCoinAdd",
                "소유 코인 정보 : ${userIdx} , ${coinIdx} , " +
                        "${accountIdx}, ${priceAvg}, ${amount}"
            )
            val psnCoinService = PsnCoinService()
            psnCoinService.setPsnCoinAddView(this)
            psnCoinService.getPsnCoinAdd(psnCoinAddinfo)
            // 거래 내역 생성 API
            var portIdx=25
            var fee = binding.possessionCoinAddFeeInputET.text.toString()
            var category = "buy"
            var memo=binding.possessionCoinAddMemoInputET.text.toString()
            var date=binding.possessionCoinAddDateInputET.text.toString()
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