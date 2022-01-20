package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinManagementBinding

class PossessionCoinManagementFragment : Fragment() {
    lateinit var binding: FragmentPossessionCoinManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPossessionCoinManagementBinding.inflate(inflater, container, false)

        binding.possessionCoinManagementAddTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.temp, PossessionCoinSearchFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.temp, PossessionCoinModifyFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}

//    fun setListener(){
//        binding.possessionCoinManagementPresentPriceOffTV.setOnClickListener {
//            binding.possessionCoinManagementPresentPriceOnTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementProfitOnBT.visibility=View.GONE
//            binding.possessionCoinManagementAverageunitPriceOnBT.visibility=View.GONE
//        }
//
//        binding.possessionCoinManagementProfitOffBT.setOnClickListener {
//            binding.possessionCoinManagementProfitOnBT.visibility=View.VISIBLE
//            binding.possessionCoinManagementPresentPriceOnTV.visibility=View.GONE
//            binding.possessionCoinManagementPresentPriceOffTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementAverageunitPriceOnBT.visibility=View.GONE
//        }
//
//        binding.possessionCoinManagementAverageunitPriceOffBT.setOnClickListener {
//            binding.possessionCoinManagementAverageunitPriceOnBT.visibility=View.VISIBLE
//            binding.possessionCoinManagementPresentPriceOnTV.visibility=View.GONE
//            binding.possessionCoinManagementPresentPriceOffTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementProfitOnBT.visibility=View.GONE
//        }
//
//        binding.possessionCoinManagementDeleteOffButtonIB.setOnClickListener {
//            binding.possessionCoinManagementDeleteOnButtonIB.visibility=View.VISIBLE
//            binding.possessionCoinManagementDeleteOffButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementAddTV.visibility=View.GONE
//            binding.possessionCoinManagementDeleteConfirmButtonTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementModifyConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementModifyOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementModifyOffButtonIB.visibility=View.VISIBLE
//        }
//
//        binding.possessionCoinManagementDeleteOnButtonIB.setOnClickListener {
//            binding.possessionCoinManagementDeleteOffButtonIB.visibility=View.VISIBLE
//            binding.possessionCoinManagementDeleteOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementAddTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementDeleteConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementModifyConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementModifyOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementModifyOffButtonIB.visibility=View.VISIBLE
//        }
//
//        binding.possessionCoinManagementModifyOffButtonIB.setOnClickListener {
//            binding.possessionCoinManagementModifyOnButtonIB.visibility=View.VISIBLE
//            binding.possessionCoinManagementModifyOffButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementAddTV.visibility=View.GONE
//            binding.possessionCoinManagementModifyConfirmButtonTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementDeleteConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementDeleteOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementDeleteOffButtonIB.visibility=View.VISIBLE
//        }
//
//        binding.possessionCoinManagementModifyOnButtonIB.setOnClickListener {
//            binding.possessionCoinManagementModifyOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementModifyOffButtonIB.visibility=View.VISIBLE
//            binding.possessionCoinManagementAddTV.visibility=View.VISIBLE
//            binding.possessionCoinManagementModifyConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementDeleteConfirmButtonTV.visibility=View.GONE
//            binding.possessionCoinManagementDeleteOnButtonIB.visibility=View.GONE
//            binding.possessionCoinManagementDeleteOffButtonIB.visibility=View.VISIBLE
//        }
//    }


//override fun onCreateView(
//    inflater: LayoutInflater, container: ViewGroup?,
//    savedInstanceState: Bundle?
//): View? {
//    binding = FragmentPossessionCoinManagementBinding.inflate(inflater , container , false)
//    return binding.root
//}