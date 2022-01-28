package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.databinding.ListItemRepresentCoinBinding

class HomeRCRVAdapter(var list:ArrayList<RepresentCoinResult>) :RecyclerView.Adapter<HomeRCRVAdapter.MyViewHolder>(){

    inner class MyViewHolder(val binding:ListItemRepresentCoinBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RepresentCoinResult){
            binding.representCoinNameTv.text = item.coinName
            binding.representCoinSymbolTv.text = item.symbol
            //binding.representCoinIv.setImageBitmap() .이미지 셋팅됐을시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRCRVAdapter.MyViewHolder {
        val binding = ListItemRepresentCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeRCRVAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}