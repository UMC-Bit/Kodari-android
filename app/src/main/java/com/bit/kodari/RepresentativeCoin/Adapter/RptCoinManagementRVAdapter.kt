package com.bit.kodari.Main.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResult
import com.bit.kodari.databinding.ItemRepresentativeCoinManagementCoinListBinding
import com.bumptech.glide.Glide

class RptCoinManagementAdapter(var rptCoinList:ArrayList<RptCoinMgtInsquireResult>): RecyclerView.Adapter<RptCoinManagementAdapter.RepresentativeCoinManagementViewHolder>(){

    interface MyItemClickListener {
        fun onItemClick(item: RptCoinMgtInsquireResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class RepresentativeCoinManagementViewHolder(val binding: ItemRepresentativeCoinManagementCoinListBinding): RecyclerView.ViewHolder(binding.root){
        val imageView: ImageView =binding.itemRepresentativeCoinManagementCoinListImageIV

        fun bind(item : RptCoinMgtInsquireResult){ // 서버에서 받아와서 보여줄 것만
            // 코인 이미지, 코인 이름, 코인 심볼, 현재가, 평가 순익, 매수 평단가
            Glide.with(imageView).load(item.coinImg).into(imageView)
            binding.itemRepresentativeCoinManagementCoinListCoinNameTV.text = item.coinName
            binding.itemRepresentativeCoinManagementCoinListCoinSymbolTV.text = item.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RptCoinManagementAdapter.RepresentativeCoinManagementViewHolder {
        val binding = ItemRepresentativeCoinManagementCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepresentativeCoinManagementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeCoinManagementViewHolder, position: Int) {
        holder.bind(rptCoinList[position])
    }
    override fun getItemCount()=rptCoinList.size
}