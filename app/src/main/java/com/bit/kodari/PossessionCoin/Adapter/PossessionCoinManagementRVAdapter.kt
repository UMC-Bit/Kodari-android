package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.databinding.ItemPossessionCoinManagementCoinListBinding


class PossessionCoinManagementAdapter(var possessionCoinList:ArrayList<PsnCoinMgtInsquireResult>): RecyclerView.Adapter<PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder>(){

    interface MyItemClickListener {
        fun onItemClick(item: PsnCoinMgtInsquireResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class PossessionCoinManagementViewHolder(val binding: ItemPossessionCoinManagementCoinListBinding) :  RecyclerView.ViewHolder(binding.root){

        val imageView: ImageView =binding.itemPossessionCoinManagementCoinListImageIV

        fun bind(item : PsnCoinMgtInsquireResult){ // 서버에서 받아와서 보여줄 것만
            // 코인 이미지, 코인 이름, 코인 심볼, 현재가, 평가 순익, 매수 평단가
//            Glide.with(imageView).load(item.coinImg).into(imageView)
            binding.itemPossessionCoinManagementCoinListCoinNameTV.text = item.coinName
//            binding.itemPossessionCoinManagementCoinListCoinSymbolTV.text = item.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinManagementAdapter.PossessionCoinManagementViewHolder {

        val binding = ItemPossessionCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PossessionCoinManagementViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PossessionCoinManagementViewHolder, position: Int) {
        holder.bind(possessionCoinList[position])
//        var SearchCoinList = searchcoinList[position]
        holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.setOnClickListener {
            mItemClickListener.onItemClick(possessionCoinList[position])
            holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility =
                View.GONE
            holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                View.VISIBLE
        }

        holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.setOnClickListener {
            holder.binding.itemPossessionCoinManagementCoinListSelectOnIV.visibility =
                View.GONE
            holder.binding.itemPossessionCoinManagementCoinListSelectOffIV.visibility =
                View.VISIBLE
        }
    }

    override fun getItemCount()=possessionCoinList.size
}