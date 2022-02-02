package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.databinding.ListMyCoinBinding

class HomePCRVAdapter(var list:ArrayList<PossesionCoinResult>) :RecyclerView.Adapter<HomePCRVAdapter.MyViewHolder>(){

    inner class MyViewHolder(val binding:ListMyCoinBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : PossesionCoinResult){
            binding.myCoinNameTv.text = item.coinName
            binding.myUnitPriceTv.text = item.priceAvg.toString()
            //binding.representCoinSymbolTv.text = item.symbol
            //binding.representCoinIv.setImageBitmap() .이미지 셋팅됐을시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePCRVAdapter.MyViewHolder {
        val binding = ListMyCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePCRVAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}