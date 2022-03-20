package com.bit.kodari.Portfolio.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.Portfolio.PortfolioManagementFragment
import com.bit.kodari.Portfolio.PortfolioModifyCoinFragment
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.R
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bumptech.glide.Glide
import java.io.Serializable

class ManagementRVAdapter(portfolioManagementFragment: PortfolioManagementFragment, val psnCoinList:ArrayList<CoinDataResponse>) : RecyclerView.Adapter<ManagementRVAdapter.ManagementViewHolder>() {
    val portfolioManagementFragment = portfolioManagementFragment

    inner class ManagementViewHolder(val binding:ListCoinItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(item:CoinDataResponse, position: Int){
            Glide.with(binding.coinIv)
                .load(item.coinImg)
                .into(binding.coinIv)
            binding.coinNameTv.text = item.coinName
            binding.coinSymbolTv.text = item.symbol
            binding.coinSelectBtnIv.setOnClickListener {
                // psnCoinList.get(position)으로 portfolioModifyCoinFragment 불러오기
                portfolioManagementFragment.requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container_fl , PortfolioModifyCoinFragment(position).apply{
                        arguments = Bundle().apply{
                            val coinModifyResponse = psnCoinList.get(position)
                            putSerializable("coinModifyResponse", coinModifyResponse)

                        }
                    })
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun onBindViewHolder(holder: ManagementViewHolder, position: Int) {
        holder.bind(psnCoinList[position],position)
    }

    override fun getItemCount(): Int = psnCoinList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagementViewHolder {
        val binding = ListCoinItemBinding.inflate(LayoutInflater.from(parent.context) ,parent,false)
        return ManagementViewHolder(binding)
    }

    fun add(item:CoinDataResponse){
        psnCoinList.add(item)
        notifyItemInserted(psnCoinList.size-1)
    }

    fun getList() : ArrayList<CoinDataResponse>{
        return psnCoinList
    }
}