package com.bit.kodari.Main.Adapter

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Util.formatD
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.Util.getPriceColor
import com.bit.kodari.databinding.ListMyCoinBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class HomePCRVAdapter(var list:List<PossesionCoinResult>) :RecyclerView.Adapter<HomePCRVAdapter.MyViewHolder>(){
    interface MyItemClickListener{
        fun onClickItem(item:PossesionCoinResult)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(myItemClickListener: MyItemClickListener){
        this.mItemClickListener= myItemClickListener
    }

    inner class MyViewHolder(val binding:ListMyCoinBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : PossesionCoinResult){
            val color = getPriceColor(item.change)
            // 바인딩
            binding.myCoinNameTv.text = item.coinName
            binding.myCoinSymbolTv.text = item.symbol
            Glide.with(binding.myCoinIv)
                .load(item.coinImg)
                .into(binding.myCoinIv)
            binding.myNowPriceTv.text = formatPrice(item.marketPrice)
            binding.myNowPriceTv.setTextColor(color)
            if(item.profit < 0){
                binding.myProfitTv.setTextColor(Color.BLUE)
                binding.myProfitTv.text = formatPrice(item.profit)
            }else{
                binding.myProfitPercentTv.setTextColor(Color.RED)
                binding.myProfitTv.text = "+" + formatPrice(item.profit)
            }
            if(item.profitRate < 0){
                binding.myProfitPercentTv.setTextColor(Color.BLUE)
                binding.myProfitPercentTv.text = formatD(item.profitRate) + "%"
            }else{
                binding.myProfitPercentTv.setTextColor(Color.RED)
                binding.myProfitPercentTv.text = formatD(item.profitRate) + "%"
            }
            binding.myUnitPriceTv.text = formatPrice(item.priceAvg)
        //binding.representCoinSymbolTv.text = item.symbol
            //binding.representCoinIv.setImageBitmap() .이미지 셋팅됐을시
        }
    }
    fun setData(userCoinList: ArrayList<PossesionCoinResult>, position: Int) {
        list = userCoinList
        notifyItemChanged(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePCRVAdapter.MyViewHolder {
        val binding = ListMyCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePCRVAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.root.setOnClickListener { mItemClickListener.onClickItem(list[position]) }
    }

    override fun getItemCount(): Int = list.size


}