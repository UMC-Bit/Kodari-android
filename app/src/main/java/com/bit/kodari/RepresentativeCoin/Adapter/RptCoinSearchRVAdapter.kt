package com.bit.kodari.RepresentativeCoin.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResult
import com.bit.kodari.databinding.ItemRepresentativeCoinSearchCoinListBinding
import com.bumptech.glide.Glide

class RptCoinSearchRVAdapter(var searchCoinList:ArrayList<RptCoinSearchResult>): RecyclerView.Adapter<RptCoinSearchRVAdapter.RptCoinSearchViewHolder>()  {
    interface MyItemClickListener {
        fun onItemClick(item:RptCoinSearchResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class RptCoinSearchViewHolder(val binding: ItemRepresentativeCoinSearchCoinListBinding): RecyclerView.ViewHolder(binding.root){

        val imageView: ImageView =binding.itemRepresentativeCoinSearchCoinListImageIV

        fun bind(item : RptCoinSearchResult){ // 서버에서 받아와서 보여줄 것만
            Glide.with(imageView).load(item.coinImg).into(imageView)
            binding.itemRepresentativeCoinSearchCoinListCoinNameTV.text = item.coinName
            binding.itemRepresentativeCoinSearchCoinListCoinSymbolTV.text = item.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RptCoinSearchRVAdapter.RptCoinSearchViewHolder {
        val binding = ItemRepresentativeCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RptCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RptCoinSearchViewHolder, position: Int) {
        holder.bind(searchCoinList[position])
//        var SearchCoinList = searchCoinList[position]
        holder.binding.itemRepresentativeCoinSearchCoinCheckOffIV.setOnClickListener {
            mItemClickListener.onItemClick(searchCoinList[position])
        }
    }


    override fun getItemCount()=searchCoinList.size

    fun filterList(filteredList: ArrayList<RptCoinSearchResult>) {

        searchCoinList = filteredList
        Log.d("searchcoinList", "${searchCoinList.size}")
        Log.d("filterList", "${filteredList.size}")
        notifyDataSetChanged()
    }
}