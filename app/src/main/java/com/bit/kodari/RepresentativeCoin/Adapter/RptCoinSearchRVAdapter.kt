package com.bit.kodari.RepresentativeCoin.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinSearchResult
import com.bit.kodari.databinding.ItemRepresentativeCoinSearchCoinListBinding
import com.bumptech.glide.Glide

class RptCoinSearchRVAdapter(var searchCoinList:ArrayList<RptCoinSearchResult>): RecyclerView.Adapter<RptCoinSearchRVAdapter.RptCoinSearchViewHolder>()  {

    interface MyItemClickListener {
        fun onItemCheck(item:RptCoinSearchResult)
        fun onItemUnCheck(item:RptCoinSearchResult)
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
            if(item.isCheck){
                binding.itemRepresentativeCoinSearchCoinCheckOnIV.visibility = View.VISIBLE
                binding.itemRepresentativeCoinSearchCoinCheckOffIV.visibility =View.GONE
            }else{
                binding.itemRepresentativeCoinSearchCoinCheckOnIV.visibility = View.GONE
                binding.itemRepresentativeCoinSearchCoinCheckOffIV.visibility =View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RptCoinSearchRVAdapter.RptCoinSearchViewHolder {
        val binding = ItemRepresentativeCoinSearchCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RptCoinSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RptCoinSearchViewHolder, position: Int) {
        holder.bind(searchCoinList[position])

        holder.binding.itemRepresentativeCoinSearchCoinCheckOffIV.setOnClickListener {
            mItemClickListener.onItemCheck(searchCoinList[position])
            holder.binding.itemRepresentativeCoinSearchCoinCheckOffIV.visibility=View.GONE
            holder.binding.itemRepresentativeCoinSearchCoinCheckOnIV.visibility= View.VISIBLE
        }

        holder.binding.itemRepresentativeCoinSearchCoinCheckOnIV.setOnClickListener {
            mItemClickListener.onItemUnCheck(searchCoinList[position])
            holder.binding.itemRepresentativeCoinSearchCoinCheckOnIV.visibility=View.GONE
            holder.binding.itemRepresentativeCoinSearchCoinCheckOffIV.visibility=View.VISIBLE
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