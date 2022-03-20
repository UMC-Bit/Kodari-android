package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Util.formatD
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.Util.getPriceColor
import com.bit.kodari.databinding.ListItemRepresentCoinBinding
import com.bumptech.glide.Glide

class HomeRCRVAdapter(var list:List<RepresentCoinResult>) :RecyclerView.Adapter<HomeRCRVAdapter.MyViewHolder>(){
    inner class MyViewHolder(val binding:ListItemRepresentCoinBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RepresentCoinResult){
            val color = getPriceColor(item.change)
            binding.representUpbitPriceTv.setTextColor(color)
            binding.representUpbitPriceTv.text = formatPrice(item.upbitPrice)
            Glide.with(binding.representCoinIv)
                .load(item.coinImg)
                .into(binding.representCoinIv)
            if(item.binancePrice == 0.0){ // 바이낸스 미 상장 코인 시세 공백처리
                binding.representBinanacePriceTv.text = ""
                binding.representKimchiPriceTv.text = ""
            }else{
                binding.representBinanacePriceTv.text = formatPrice(item.binancePrice)
                binding.representKimchiPriceTv.text = formatD(item.kimchi) +"%"
            }
            binding.representCoinNameTv.text = item.coinName
            binding.representCoinSymbolTv.text = item.symbol

        }
    }

    fun setData(representCoinList: ArrayList<RepresentCoinResult>, position: Int) {
        list = representCoinList
        notifyItemChanged(position)
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

