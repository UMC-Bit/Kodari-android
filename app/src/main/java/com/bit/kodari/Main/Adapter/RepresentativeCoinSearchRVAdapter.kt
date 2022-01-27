package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.databinding.ItemRepresentativeCoinSearchCoinListBinding

class SearchCoinData(val coinImage:Int, val coinName:String, val coinSymbol:String, val coinMove:Int)

class RepresentativeCoinSearchAdapter(val searchCoinList:ArrayList<SearchCoinData>): RecyclerView.Adapter<RepresentativeCoinSearchAdapter.RepresentativeCoinSearchViewHolder>() {

    inner class RepresentativeCoinSearchViewHolder(val binding: ItemRepresentativeCoinSearchCoinListBinding): RecyclerView.ViewHolder(binding.root){
        val coinImage=binding.itemRepresentativeCoinSearchCoinListImageIV
        val coinName=binding.itemRepresentativeCoinSearchCoinListCoinNameTV
        val coinSymbol=binding.itemRepresentativeCoinSearchCoinListCoinSymbolTV
        val coinMove=binding.itemRepresentativeCoinSearchCoinListMoveButtonIV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeCoinSearchAdapter.RepresentativeCoinSearchViewHolder {
        val binding = ItemRepresentativeCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepresentativeCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeCoinSearchViewHolder, position: Int) {
        holder.coinImage.setImageResource(searchCoinList[position].coinImage)
        holder.coinName.text = searchCoinList[position].coinName
        holder.coinSymbol.text = searchCoinList[position].coinSymbol
        holder.coinMove.setImageResource(searchCoinList[position].coinMove)
    }

    override fun getItemCount()=searchCoinList.size
}