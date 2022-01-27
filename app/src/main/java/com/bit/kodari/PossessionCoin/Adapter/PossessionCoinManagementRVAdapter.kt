package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.databinding.ItemPossessionCoinManagementCoinListBinding

class PossessionCoinData(val coinImage:Int, val coinName:String, val coinSymbol:String, val presentPrice:String, val profit:String, val averagePrice:String, val select:Int)

class PossessionCoinManagementAdapter(val possessionCoinList:ArrayList<PossessionCoinData>): RecyclerView.Adapter<PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder>(){

    inner class PossessionCoinManagementViewHolder(val binding: ItemPossessionCoinManagementCoinListBinding) :  RecyclerView.ViewHolder(binding.root){
        val coinImage = binding.itemPossessionCoinManagementCoinListImageIV
        val coinName = binding.itemPossessionCoinManagementCoinListCoinNameTV
        val coinSymbol = binding.itemPossessionCoinManagementCoinListCoinSymbolTV
        val presentPrice = binding.itemPossessionCoinManagementCoinListPriceTV
        val profit = binding.itemPossessionCoinManagementCoinListProfitPlusTV
        val averagePrice = binding.itemPossessionCoinManagementCoinListAverageunitPriceTV
        val select = binding.itemPossessionCoinManagementCoinListSelectOffIV


        init {
            binding.itemPossessionCoinManagementCoinListSelectOffIV.setOnClickListener {
                binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility =
                    View.GONE
                binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                    View.VISIBLE
            }

            binding.itemPossessionCoinManagementCoinListSelectOnIV.setOnClickListener {
                binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                    View.GONE
                binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility =
                    View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder {

        val binding = ItemPossessionCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PossessionCoinManagementViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PossessionCoinManagementViewHolder, position: Int) {
        holder.coinImage.setImageResource(possessionCoinList[position].coinImage)
        holder.coinName.text = possessionCoinList[position].coinName
        holder.coinSymbol.text = possessionCoinList[position].coinSymbol
        holder.presentPrice.text = possessionCoinList[position].presentPrice
        holder.profit.text = possessionCoinList[position].profit
        holder.averagePrice.text = possessionCoinList[position].averagePrice
        holder.select.setImageResource(possessionCoinList[position].select)
    }

    override fun getItemCount()=possessionCoinList.size
}