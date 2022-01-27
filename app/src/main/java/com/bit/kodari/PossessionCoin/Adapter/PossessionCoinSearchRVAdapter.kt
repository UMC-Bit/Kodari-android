package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.databinding.ItemPossessionCoinSearchCoinListBinding

class SearchCoinData(val coinImage:Int, val coinName:String, val coinSymbol:String, val coinMove:Int)

class PossessionCoinSearchAdapter(val searchcoinList:ArrayList<SearchCoinData>): RecyclerView.Adapter<PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder>() {

    inner class PossessionCoinSearchViewHolder(val binding: ItemPossessionCoinSearchCoinListBinding): RecyclerView.ViewHolder(binding.root){
        val coinImage=binding.itemPossessionCoinSearchCoinListImageIV
        val coinName=binding.itemPossessionCoinSearchCoinListCoinNameTV
        val coinSymbol=binding.itemPossessionCoinSearchCoinListCoinSymbolTV
        val coinMove=binding.itemPossessionCoinSearchCoinListMoveButtonIV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder {
       val binding = ItemPossessionCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PossessionCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PossessionCoinSearchViewHolder, position: Int) {
        holder.coinImage.setImageResource(searchcoinList[position].coinImage)
        holder.coinName.text = searchcoinList[position].coinName
        holder.coinSymbol.text = searchcoinList[position].coinSymbol
        holder.coinMove.setImageResource(searchcoinList[position].coinMove)
    }

    override fun getItemCount()=searchcoinList.size
}