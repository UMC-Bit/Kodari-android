package com.bit.kodari.PossessionCoin.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.Util.getPriceColor
import com.bit.kodari.databinding.ItemPossessionCoinManagementCoinListBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat


class PossessionCoinManagementAdapter(var possessionCoinList:ArrayList<PossesionCoinResult>): RecyclerView.Adapter<PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder>(){
    companion object{
        var isClick=false
        var clickPosition: Int = -1
        var isClickMap = HashMap<Int, Boolean>()
    }

    interface MyItemClickListener {
        fun onItemClick(item: PossesionCoinResult)
    }

    public fun getClick():Boolean{
        return isClick
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
            binding.itemPossessionCoinManagementCoinListPriceAvgTV.text = formatPrice(item.priceAvg)
            binding.itemPossessionCoinManagementCoinListCoinSymbolTV.text = item.symbol
            binding.itemPossessionCoinManagementCoinListPriceTV.text = formatPrice(item.upbitPrice)
            if(item.profit < 0)
                binding.itemPossessionCoinManagementCoinListProfitPlusTV.setTextColor(Color.BLUE)
            binding.itemPossessionCoinManagementCoinListProfitPlusTV.text = formatPrice(item.profit)

            if(item.isChecked){
                binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility = View.VISIBLE
                binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility = View.GONE
            } else {
                binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility = View.VISIBLE
                binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility = View.GONE
            }
        }
    }
    fun setData(userCoinList: ArrayList<PossesionCoinResult>, position: Int) {
        possessionCoinList = userCoinList
        notifyItemChanged(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder {
        val binding = ItemPossessionCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PossessionCoinManagementViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PossessionCoinManagementViewHolder, position: Int) {
        holder.bind(possessionCoinList[position])
//        isClickMap[position] = false
            holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.setOnClickListener {
                Log.d("PossessiongAdatper" , "${isClick} 와 ${isClickMap[position]}")
                //if(!isClick)
               //{
                    Log.d("버튼 클릭" , "실행")
                    possessionCoinList[position].isChecked = true
                    clickPosition = position
                    isClick=true
                    isClickMap[position] = true
                    mItemClickListener.onItemClick(possessionCoinList[position])
                    holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                        View.VISIBLE
                    //다른 애들은 체크 해제해줘야함
                    for( num in 0 until possessionCoinList.size){
                        if(num == clickPosition) continue
                        possessionCoinList[num].isChecked = false       //나 이외에 것들 전부 false로 바꿈
                        //바꾼 후 재 갱신 해줘야함 ..
                    }
                    notifyDataSetChanged()
                    Log.d("PossessiongCoinDelete" , " isClick : ${isClick}, isClickMap[position] : ${isClickMap[position]}")
               // }
            }

            holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.setOnClickListener {
                Log.d("PossessiongAdatper" , "${isClick} 와 ${isClickMap[position]}")
                if(isClick && isClickMap[position] == true)
                {
                    possessionCoinList[position].isChecked = false
                    isClick=false
                    holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                        View.GONE
                    holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility = View.VISIBLE
                }
            }


    }
    override fun getItemCount()=possessionCoinList.size
}