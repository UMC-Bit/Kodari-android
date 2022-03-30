package com.bit.kodari.Main.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult
import com.bit.kodari.Util.formatD
import com.bit.kodari.Util.formatPrice
import com.bit.kodari.Util.getPriceColor
import com.bit.kodari.databinding.ItemRepresentativeCoinManagementCoinListBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class RptCoinManagementAdapter(var rptCoinList:ArrayList<RepresentCoinResult>): RecyclerView.Adapter<RptCoinManagementAdapter.RepresentativeCoinManagementViewHolder>(){
    interface MyItemClickListener {
        fun onItemCheck(item: RepresentCoinResult)
        fun onItemUnCheck(item:RepresentCoinResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class RepresentativeCoinManagementViewHolder(val binding: ItemRepresentativeCoinManagementCoinListBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView =binding.itemRepresentativeCoinManagementCoinListImageIV

        fun bind(item: RepresentCoinResult){ // 서버에서 받아와서 보여줄 것만
            val color = getPriceColor(item.change)
            // 코인 이미지, 코인 이름, 코인 심볼, 현재가, 평가 순익, 매수 평단가
            Glide.with(imageView).load(item.coinImg).into(imageView)
            binding.itemRepresentativeCoinManagementCoinListCoinNameTV.text = item.coinName
            binding.itemRepresentativeCoinManagementCoinListCoinSymbolTV.text = item.symbol
            if(item.binancePrice == 0.0){ // 바이낸스 미 상장 코인 시세 공백처리
                binding.itemRepresentativeCoinManagementCoinListBitfinexPriceTV.text = ""
                binding.itemRepresentativeCoinManagementCoinListKimchiPremiumTV.text = ""
            }else{
                binding.itemRepresentativeCoinManagementCoinListBitfinexPriceTV.text = formatPrice(item.binancePrice)
                binding.itemRepresentativeCoinManagementCoinListKimchiPremiumTV.text = formatD(item.kimchi) +"%"
            }
            binding.itemRepresentativeCoinManagementCoinUpbitPriceTV.text = formatPrice(item.marketPrice)
            binding.itemRepresentativeCoinManagementCoinUpbitPriceTV.setTextColor(color)

            //업비트, 바이낸스 가격 추가하기.


            //클릭했을때 체크 활성화하기.
            if(item.isChecked){
                binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.VISIBLE
                binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility = View.GONE
            } else{
                binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility = View.VISIBLE
                binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.GONE
            }
            Log.d("대표코인", "갱신")

        }
    }
    fun setData(representCoinList: ArrayList<RepresentCoinResult>, position: Int) {
        rptCoinList = representCoinList
        notifyItemChanged(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RptCoinManagementAdapter.RepresentativeCoinManagementViewHolder {
        val binding = ItemRepresentativeCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepresentativeCoinManagementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeCoinManagementViewHolder, position: Int) {
        holder.bind(rptCoinList[position])
        holder.binding.itemRepresentativeCoinManagementCoinListSelectOnIV.setOnClickListener {
            holder.binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.GONE
            holder.binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility =View.VISIBLE
            mItemClickListener.onItemUnCheck(rptCoinList[position])
        }

        holder.binding.itemRepresentativeCoinManagementCoinListSelectOffIV.setOnClickListener {
            holder.binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility = View.GONE
            holder.binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.VISIBLE
            mItemClickListener.onItemCheck(rptCoinList[position])
        }
    }
    override fun getItemCount() = rptCoinList.size
}