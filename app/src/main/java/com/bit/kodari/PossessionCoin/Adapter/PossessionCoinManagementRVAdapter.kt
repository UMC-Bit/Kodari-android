package com.bit.kodari.PossessionCoin.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.databinding.ItemPossessionCoinManagementCoinListBinding

//class PossessionCoinManagementRVAdapter() : RecyclerView.Adapter<PossessionCoinManagementRVAdapter.ViewHolder>() {
//    private val coins = ArrayList<Coin>()
//
//    interface MyItemClickListener{
//        fun onRemoveSong(songId: Int)
//    }
//
//    private lateinit var mItemClickListener: MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PossessionCoinManagementRVAdapter.ViewHolder {
//        val binding: ItemPossessionCoinManagementCoinListBinding = ItemPossessionCoinManagementCoinListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
//
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: PossessionCoinManagementRVAdapter.ViewHolder, position: Int) {
//        holder.bind(coins[position])
//        holder.binding.itemSongMoreIv.setOnClickListener {
//            mItemClickListener.onRemoveSong(coins[position].id)
//            removeSong(position)
//        }
//    }
//
//    override fun getItemCount(): Int = coins.size
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun addSongs(coins: ArrayList<Coin>) {
//        this.coins.clear()
//        this.coins.addAll(coins)
//        notifyDataSetChanged()
//    }
//
//    fun removeSong(position: Int){
//        coins.removeAt(position)
//        notifyDataSetChanged()
//    }
//
//    inner class ViewHolder(val binding: ItemPossessionCoinManagementCoinListBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(coin: Coin){
//        }
//    }
//}