package com.bit.kodari.PossessionCoin.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinSearchResult
import com.bit.kodari.databinding.ItemPossessionCoinSearchCoinListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PossessionCoinSearchAdapter(var searchcoinList:ArrayList<PsnCoinSearchResult>): RecyclerView.Adapter<PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item:PsnCoinSearchResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister:MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class PossessionCoinSearchViewHolder(val binding: ItemPossessionCoinSearchCoinListBinding): RecyclerView.ViewHolder(binding.root){

        val imageView: ImageView=binding.itemPossessionCoinSearchCoinListImageIV

        fun bind(item : PsnCoinSearchResult){ // 서버에서 받아와서 보여줄 것만
            Glide.with(imageView).load(item.coinImg)
                .into(imageView)
            binding.itemPossessionCoinSearchCoinListCoinNameTV.text = item.coinName
            binding.itemPossessionCoinSearchCoinListCoinSymbolTV.text = item.symbol

            //binding.representCoinIv.setImageBitmap() .이미지 셋팅됐을시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinSearchAdapter.PossessionCoinSearchViewHolder {
       val binding = ItemPossessionCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PossessionCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PossessionCoinSearchViewHolder, position: Int) {
        holder.bind(searchcoinList[position])
        holder.binding.itemPossessionCoinSearchCoinListMoveButtonIV.setOnClickListener {
            mItemClickListener.onItemClick(searchcoinList[position])
        }
    }

    override fun getItemCount()=searchcoinList.size

    fun filterList(filteredList: ArrayList<PsnCoinSearchResult>) {

        searchcoinList = filteredList
        Log.d("searchcoinList", "${searchcoinList.size}")
        Log.d("filterList", "${filteredList.size}")
        notifyDataSetChanged()
    }
}