package com.bit.kodari.Portfolio.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Portfolio.Data.SearchCoinResult
import com.bit.kodari.databinding.ListCoinItemBinding
import com.bumptech.glide.Glide

class SearchCoinRVAdapter(var searchCoinList:ArrayList<SearchCoinResult>): RecyclerView.Adapter<SearchCoinRVAdapter.SearchCoinViewHolder>()  {
    interface MyItemClickListener {
        fun onItemClick(item:SearchCoinResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class SearchCoinViewHolder(val binding: ListCoinItemBinding): RecyclerView.ViewHolder(binding.root){

        val imageView: ImageView =binding.coinIv

        fun bind(item : SearchCoinResult){ // 서버에서 받아와서 보여줄 것만
            Glide.with(imageView).load(item.coinImg)
                .into(imageView)
            binding.coinNameTv.text = item.coinName
            binding.coinSymbolTv.text = item.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCoinRVAdapter.SearchCoinViewHolder {
        val binding = ListCoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchCoinViewHolder, position: Int) {
        holder.bind(searchCoinList[position])
        holder.binding.root.setOnClickListener {
            mItemClickListener.onItemClick(searchCoinList[position])
        }
    }


    override fun getItemCount()=searchCoinList.size

    fun filterList(filteredList: ArrayList<SearchCoinResult>) {

        searchCoinList = filteredList
        Log.d("searchCoinList", "${searchCoinList.size}")
        Log.d("filterList", "${filteredList.size}")
        notifyDataSetChanged()
    }
}

