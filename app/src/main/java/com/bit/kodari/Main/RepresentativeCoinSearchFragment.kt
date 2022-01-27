package com.bit.kodari.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.kodari.Main.Adapter.RepresentativeCoinManagementAdapter
import com.bit.kodari.Main.Adapter.RepresentativeCoinSearchAdapter
import com.bit.kodari.Main.Adapter.SearchCoinData
import com.bit.kodari.R
import com.bit.kodari.databinding.FragmentRepresentativeCoinSearchBinding

class RepresentativeCoinSearchFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeCoinSearchBinding

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
        binding = FragmentRepresentativeCoinSearchBinding.inflate(inflater , container , false)

        binding.representativeCoinSearchCoinListRV.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.representativeCoinSearchCoinListRV.adapter= RepresentativeCoinSearchAdapter(SearchCoinList)



        binding.representativeCoinSearchBackIV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
        }

        binding.representativeCoinSearchCompleteTV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container_fl, RepresentativeCoinManagementFragment()).commitAllowingStateLoss()
        }

        return binding.root
    }
}