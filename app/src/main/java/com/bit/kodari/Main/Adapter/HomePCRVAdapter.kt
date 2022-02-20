package com.bit.kodari.Main.Adapter

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.databinding.ListMyCoinBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class HomePCRVAdapter(var list:List<PossesionCoinResult>) :RecyclerView.Adapter<HomePCRVAdapter.MyViewHolder>(){
    private var df: DecimalFormat = DecimalFormat("#.##")

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
            binding.myNowPriceTv.text = formatPrice(item.upbitPrice) +"원"
            binding.myNowPriceTv.setTextColor(color)
            if(item.profit < 0){
                binding.myProfitTv.setTextColor(Color.BLUE)
                binding.myProfitTv.text = formatPrice(item.profit) + "원"
            }else{
                binding.myProfitTv.text = "+" + formatPrice(item.profit) + "원"

            }
            binding.myUnitPriceTv.text = formatPrice(item.priceAvg) + "원"
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
        holder.itemView.setOnClickListener { mItemClickListener.onClickItem(list[position]) }
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
        }else if(number<=-1 && number>-10){
            price = String.format("%.2f", number)
        }else if(number<=-10 && number>-100){
            price = String.format("%.1f", number)
        }else if(number <=-100){
            val price_ = String.format("%.0f", number).toDouble()
            price = formatD(price_)
        }else{
            price = String.format("%.5f", number)
        }
        return price;
    }
    fun getPriceColor(change: Double): Int{
        // 상승, 하락, 보합
        var colorUpbit = 0 // 업비트
        if(change == 1.0){
            colorUpbit = Color.RED
        }else if(change == -1.0){
            colorUpbit = Color.BLUE
        }else{
            colorUpbit = Color.BLACK
        }
        return colorUpbit
    }
}