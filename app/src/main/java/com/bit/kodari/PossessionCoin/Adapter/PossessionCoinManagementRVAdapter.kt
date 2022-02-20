package com.bit.kodari.PossessionCoin.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.databinding.ItemPossessionCoinManagementCoinListBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat


class PossessionCoinManagementAdapter(var possessionCoinList:ArrayList<PossesionCoinResult>): RecyclerView.Adapter<PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder>(){
    private var df: DecimalFormat = DecimalFormat("#.##")
    companion object{
        var isClick=false
        var clickPosition: Int = -1
        var isClickMap = HashMap<Int, Boolean>()
    }

    interface MyItemClickListener {
        fun onItemClick(item: PossesionCoinResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class PossessionCoinManagementViewHolder(val binding: ItemPossessionCoinManagementCoinListBinding) :  RecyclerView.ViewHolder(binding.root){

        val imageView: ImageView =binding.itemPossessionCoinManagementCoinListImageIV

        fun bind(item: PossesionCoinResult){ // 서버에서 받아와서 보여줄 것만
            // 코인 이미지, 코인 이름, 코인 심볼, 현재가, 평가 순익, 매수 평단가
            val color = getPriceColor(item.change)
            binding.itemPossessionCoinManagementCoinListPriceTV.setTextColor(color)
            Glide.with(imageView).load(item.coinImg).into(imageView)
            binding.itemPossessionCoinManagementCoinListCoinNameTV.text = item.coinName
            binding.itemPossessionCoinManagementCoinListPriceAvgTV.text = formatPrice(item.priceAvg) +"원"
            binding.itemPossessionCoinManagementCoinListCoinSymbolTV.text = item.symbol
            binding.itemPossessionCoinManagementCoinListPriceTV.text = formatPrice(item.upbitPrice) +"원"
            if(item.profit < 0)
                binding.itemPossessionCoinManagementCoinListProfitPlusTV.setTextColor(Color.BLUE)
            binding.itemPossessionCoinManagementCoinListProfitPlusTV.text = formatPrice(item.profit) +"원"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder {
        isClick=false
        val binding = ItemPossessionCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PossessionCoinManagementViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PossessionCoinManagementViewHolder, position: Int) {
        holder.bind(possessionCoinList[position])
        isClickMap[position] = false

            holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.setOnClickListener {
                if(!isClick)
                {
                    isClick=true
                    isClickMap[position] = true
                    clickPosition = position
                    mItemClickListener.onItemClick(possessionCoinList[position])
                    holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                        View.VISIBLE
                }
            }

            holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.setOnClickListener {
                if(isClick && isClickMap[position] == true)
                {
                    isClick=false
                    holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                        View.GONE
                }
            }


    }

    override fun getItemCount()=possessionCoinList.size

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