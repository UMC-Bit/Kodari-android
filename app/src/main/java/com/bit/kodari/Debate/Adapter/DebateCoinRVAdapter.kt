package com.bit.kodari.Debate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Debate.PostData.DebateCoinResult
import com.bit.kodari.R
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bumptech.glide.Glide

class DebateCoinRVAdapter(var coinList:ArrayList<DebateCoinResult>):RecyclerView.Adapter<DebateCoinRVAdapter.MyViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item:DebateCoinResult )
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }
    
    inner class MyViewHolder(val binding:ListCoinItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : DebateCoinResult) {
            if(item.coinImg != ""){
                Glide.with(binding.coinIv)
                    .load(item.coinImg)
                    .error(R.drawable.profile_image)
                    .into(binding.coinIv)
            }
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
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(coinList[position]) }
    }

    override fun getItemCount(): Int = coinList.size

    fun filterList(filteredList: ArrayList<DebateCoinResult>) {
        coinList = filteredList
        notifyDataSetChanged()
    }



}