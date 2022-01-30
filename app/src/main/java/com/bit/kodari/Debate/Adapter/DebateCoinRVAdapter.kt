package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.Data.DebateCoinResult
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bit.kodari.databinding.ListItemRepresentCoinBinding

class DebateCoinRVAdapter(var coinList:ArrayList<DebateCoinResult>):RecyclerView.Adapter<DebateCoinRVAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding:ListCoinItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : DebateCoinResult) {
            //binding.coinIv.setImageBitmap(item.coinImg)   : 코인 이미지 추가
            binding.coinSymbolTv.text = item.symbol
            binding.coinNameTv.text = item.coinName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListCoinItemBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(coinList[position])
    }

    override fun getItemCount(): Int = coinList.size

    fun filterList(filteredList: ArrayList<DebateCoinResult>) {
        coinList = filteredList
        notifyDataSetChanged()
    }


}