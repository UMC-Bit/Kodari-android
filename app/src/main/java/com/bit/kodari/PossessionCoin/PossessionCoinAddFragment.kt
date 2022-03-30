package com.bit.kodari.PossessionCoin
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.number.IntegerWidth
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.MyApplicationClass
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.SignupPwFragment
import com.bit.kodari.Main.HomeFragment
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
class PossessionCoinAddFragment(val accountName:String , val marketIdx:Int) : BaseFragment<FragmentPossessionCoinAddBinding>(FragmentPossessionCoinAddBinding::inflate) , PsnCoinAddTradeView {
    val tradeTime = StringBuilder()
    var coinIdx by Delegates.notNull<Int>()
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinSearchFragment(accountName , marketIdx)).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun initAfterBinding() {
        binding.possessionCoinAddBeforeButtonIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinSearchFragment(accountName , marketIdx)).commit()
        }
        getCoinInformation()
        setInit()
        setListener()
        //포토폴리오 이름 ...
        binding.possessionCoinAddAccountNameTV.text = accountName
    }

    //마켓인덱스 2이면 로고와 텍스트 변경
    private fun setInit() {
        if(marketIdx == 2){
            binding.possessionCoinAddExchangeLogoIV.setImageResource(R.drawable.bithumb)
            binding.possessionCoinAddExchangeNameTV.text = "빗썸"
        }
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
                binding.possessionCoinAddDateInputET.text = (tradeTime.toString())
                tradeTime.setLength(0)
            }, hour, minute, false)
        val datePicker = DatePickerDialog(context as MainActivity,
            { view, year, month, dayOfMonth ->
                val date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth)
                tradeTime.append(date)
                tradeTime.append("")
                timePicker.show()
            }, year, month, day)

        datePicker.show()
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
            var fee = 0.05
            val price = binding.possessionCoinAddPriceInputET.text.toString()
            val amount = binding.possessionCoinAddQuantityInputET.text.toString()
            // 거래 내역 생성 API
            val portIdx=MyApplicationClass.myPortIdx
            val feeText = binding.possessionCoinAddFeeInputET.text.toString()
            if(feeText.isNotEmpty())
                fee = feeText.toDouble()
            val category = "buy"
            val memo=binding.possessionCoinAddMemoInputET.text.toString()
            val date=binding.possessionCoinAddDateInputET.text.toString()
            val psnCoinAddTradeInfo = PsnCoinAddTradeInfo(
                portIdx, coinIdx, price,
                amount, fee, category,
                memo, date
            )
            Log.d(
                "psnCoinTradeAdd",
                "거래 내역 정보 : ${portIdx}, ${coinIdx}, ${price}, " +
                        "${psnCoinAddTradeInfo.amount}, ${psnCoinAddTradeInfo.fee}, ${psnCoinAddTradeInfo.category}, ${psnCoinAddTradeInfo.memo}, ${psnCoinAddTradeInfo.date}"
            )
            val psnCoinService = PsnCoinService()
            psnCoinService.setPsnCoinAddTradeView(this)
            showLoadingDialog(requireContext())
            psnCoinService.getPsnCoinAddTrade(psnCoinAddTradeInfo)
        }

        binding.possessionCoinAddDateInputET.setOnClickListener{
            datetimepicker()
        }


    }

    override fun psnCoinAddTradeSuccess(response: PsnCoinAddTradeResponse) {
        dismissLoadingDialog()
        when(response.code){
            1000 -> {
                Toast.makeText(context,"거래내역 추가 성공" , Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl, PossessionCoinManagementFragment(accountName , marketIdx)).commitAllowingStateLoss()
            }
            else -> {
                Toast.makeText(context,"거래내역 추가 실패 , ${response.message}" , Toast.LENGTH_LONG).show()
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