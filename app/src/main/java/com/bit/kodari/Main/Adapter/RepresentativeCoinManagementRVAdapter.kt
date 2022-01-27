package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.databinding.ItemRepresentativeCoinManagementCoinListBinding

class ManagementCoinData(val coinImage:Int, val coinName:String, val coinSymbol:String, val upbitPrice:String, val bitfinexPrice:String, val kimchiPremium:String, val select:Int)

class RepresentativeCoinManagementAdapter(val managementCoinList:ArrayList<ManagementCoinData>): RecyclerView.Adapter<RepresentativeCoinManagementAdapter.RepresentativeCoinManagementViewHolder>() {

    inner class RepresentativeCoinManagementViewHolder(val binding: ItemRepresentativeCoinManagementCoinListBinding): RecyclerView.ViewHolder(binding.root){
        val coinImage=binding.itemRepresentativeCoinManagementCoinListImageIV
        val coinName=binding.itemRepresentativeCoinManagementCoinListCoinNameTV
        val coinSymbol=binding.itemRepresentativeCoinManagementCoinListCoinSymbolTV
        val upbitPrice=binding.itemRepresentativeCoinManagementCoinUpbitPriceTV
        val bitfinexPrice=binding.itemRepresentativeCoinManagementCoinListBitfinexPriceTV
        val kimchiPremium=binding.itemRepresentativeCoinManagementCoinListKimchiPremiumTV
        val select=binding.itemRepresentativeCoinManagementCoinListSelectOffIV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeCoinManagementAdapter.RepresentativeCoinManagementViewHolder {
        val binding = ItemRepresentativeCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepresentativeCoinManagementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeCoinManagementViewHolder, position: Int) {
        holder.coinImage.setImageResource(managementCoinList[position].coinImage)
        holder.coinName.text = managementCoinList[position].coinName
        holder.coinSymbol.text = managementCoinList[position].coinSymbol
        holder.upbitPrice.text=managementCoinList[position].upbitPrice
        holder.bitfinexPrice.text=managementCoinList[position].bitfinexPrice
        holder.kimchiPremium.text=managementCoinList[position].kimchiPremium
        holder.select.setImageResource(managementCoinList[position].select)
    }

    override fun getItemCount()=managementCoinList.size
}