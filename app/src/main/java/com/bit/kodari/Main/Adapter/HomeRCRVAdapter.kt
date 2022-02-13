package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.databinding.ListItemRepresentCoinBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class HomeRCRVAdapter(var list:List<RepresentCoinResult>) :RecyclerView.Adapter<HomeRCRVAdapter.MyViewHolder>(){
    private var df: DecimalFormat = DecimalFormat("#.##")
    inner class MyViewHolder(val binding:ListItemRepresentCoinBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RepresentCoinResult){
            binding.representBinanacePriceTv.text = formatPrice(item.binancePrice) +"원"
            binding.representUpbitPriceTv.text = formatPrice(item.upbitPrice) +"원"
            Glide.with(binding.representCoinIv)
                .load(item.coinImg)
                .into(binding.representCoinIv)
            binding.representKimchiPriceTv.text = formatD(item.kimchi) +"%"
            binding.representCoinNameTv.text = item.coinName
            binding.representCoinSymbolTv.text = item.symbol

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

    public fun formatD(number:Double): String {
        return df.format(number)
    }

    fun formatPrice(number: Double): String{
        lateinit var price: String
        if(number>=1 && number < 10){
            price = String.format("%.2f", number)
        }else if(number>=10 && number < 100){
            price = String.format("%.1f", number)
        }else if(number>=100){
            val price_ = String.format("%.0f", number).toDouble()
            price = formatD(price_)
        }else{
            price = String.format("%.5f", number)
        }
        return price;
    }
}

