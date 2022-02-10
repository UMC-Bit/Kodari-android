package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResult
import com.bit.kodari.databinding.ItemPossessionCoinMemoBinding

class MemoRVAdapter (var memoList : ArrayList<PsnCoinGetTradeResult>):
    RecyclerView.Adapter<MemoRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding:ItemPossessionCoinMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : PsnCoinGetTradeResult){
            binding.itemPossessionCoinMemoDateTV.text = item.date.toString()
            binding.itemPossessionCoinMemoCoinNameTV.text = item.coinName
            binding.itemPossessionCoinMemoAverageunitPriceTV.text = item.price.toString()
            binding.itemPossessionCoinMemoQuantityTV.text = item.amount.toString()
            binding.itemPossessionCoinMemoProfitTV.text = item.fee.toString()
            binding.itemPossessionCoinMemoBuyMemoInputTV.text = item.memo
            if(item.category == "buy"){
                binding.itemPossessionCoinMemoBuyImageIV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoSellImageIV.visibility = View.GONE
                binding.itemPossessionCoinMemoBuyTextTV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoSellTextTV.visibility = View.GONE
            } else{
                binding.itemPossessionCoinMemoBuyImageIV.visibility = View.GONE
                binding.itemPossessionCoinMemoSellImageIV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoBuyTextTV.visibility = View.GONE
                binding.itemPossessionCoinMemoSellTextTV.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPossessionCoinMemoBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(memoList[position])
    }

    override fun getItemCount(): Int = memoList.size
}