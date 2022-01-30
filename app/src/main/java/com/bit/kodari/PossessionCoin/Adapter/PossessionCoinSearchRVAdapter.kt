package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateCoinResult
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.databinding.ItemPossessionCoinSearchCoinListBinding

class PossessionCoinSearchAdapter(val searchcoinList:ArrayList<PsnCoinSearchResult>): RecyclerView.Adapter<PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder>() {
//    interface MyItemClickListener {
//        fun onItemClick(item: DebateCoinResult)
//    }
    inner class PossessionCoinSearchViewHolder(val binding: ItemPossessionCoinSearchCoinListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item : PsnCoinSearchResult){ // 서버에서 받아와서 보여줄 것만
//            binding.itemPossessionCoinSearchCoinListImageIV.setImageResource()
            binding.itemPossessionCoinSearchCoinListCoinNameTV.text = item.coinName
            binding.itemPossessionCoinSearchCoinListCoinSymbolTV.text = item.symbol
            //binding.representCoinIv.setImageBitmap() .이미지 셋팅됐을시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder {
       val binding = ItemPossessionCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PossessionCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PossessionCoinSearchViewHolder, position: Int) {
        holder.bind(searchcoinList[position])
    }

    override fun getItemCount()=searchcoinList.size
}