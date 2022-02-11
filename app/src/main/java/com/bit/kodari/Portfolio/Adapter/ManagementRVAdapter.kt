package com.bit.kodari.Portfolio.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Portfolio.Data.CoinDataResponse
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinAddInfo
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bumptech.glide.Glide

class ManagementRVAdapter(val psnCoinList:ArrayList<CoinDataResponse>) : RecyclerView.Adapter<ManagementRVAdapter.ManagementViewHolder>() {

    inner class ManagementViewHolder(val binding:ListCoinItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(item:CoinDataResponse){
            Glide.with(binding.coinIv)
                .load(item.coinImg)
                .into(binding.coinIv)
            binding.coinNameTv.text = item.coinName
            binding.coinSymbolTv.text = item.symbol
        }
    }

    override fun onBindViewHolder(holder: ManagementViewHolder, position: Int) {
        holder.bind(psnCoinList[position])
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