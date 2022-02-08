package com.bit.kodari.PossessionCoin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding
import com.bit.kodari.databinding.FragmentPossessionCoinModifyBinding
import java.lang.StringBuilder
import java.util.*

class PossessionCoinModifyFragment : Fragment() {
    lateinit var binding:FragmentPossessionCoinModifyBinding
    val tradeTime = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinModifyBinding.inflate(inflater, container, false)

        moveLayout()
        datetimepicker()
        BuyAndSellButton()

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
                binding.possessionCoinModifyDayInputET.setText(tradeTime.toString())
                tradeTime.setLength(0)
            }, hour, minute, false)

        val datePicker = DatePickerDialog(context as MainActivity,
            { view, year, month, dayOfMonth ->
                val date = String.format("%d-%02d-%02d ", year, month + 1, dayOfMonth)
                tradeTime.append(date)
                tradeTime.append("")
                timePicker.show()
            }, year, month, day)

        binding.possessionCoinModifyDayInputET.setOnClickListener { v ->
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
}