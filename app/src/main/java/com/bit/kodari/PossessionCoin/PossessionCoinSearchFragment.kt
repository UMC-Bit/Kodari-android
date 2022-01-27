package com.bit.kodari.PossessionCoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinData
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinManagementAdapter
import com.bit.kodari.PossessionCoin.Adapter.PossessionCoinSearchAdapter
import com.bit.kodari.PossessionCoin.Adapter.SearchCoinData
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentPossessionCoinSearchBinding


class PossessionCoinSearchFragment : Fragment() {
    lateinit var binding:FragmentPossessionCoinSearchBinding

    val SearchCoinList = arrayListOf(
        SearchCoinData(R.drawable.btc, "비트코인", "btc", R.drawable.move_button),
        SearchCoinData(R.drawable.btc, "비트코인", "btc", R.drawable.move_button),
        SearchCoinData(R.drawable.btc, "비트코인", "btc", R.drawable.move_button),
        SearchCoinData(R.drawable.btc, "비트코인", "btc", R.drawable.move_button),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPossessionCoinSearchBinding.inflate(inflater , container , false)

        binding.possessionCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.possessionCoinSearchCoinListRV.adapter= PossessionCoinSearchAdapter(SearchCoinList)

        binding.tempDialogBT.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinAddFragment()).commitAllowingStateLoss()
        }

        binding.possessionCoinSearchBackIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, PossessionCoinManagementFragment()).commitAllowingStateLoss()
        }




        return binding.root
    }
}