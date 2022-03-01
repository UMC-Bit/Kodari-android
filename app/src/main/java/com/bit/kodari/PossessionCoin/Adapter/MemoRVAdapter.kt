package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.Main.Data.GetTradeListResult
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinGetTradeResult
import com.bit.kodari.databinding.ItemPossessionCoinMemoBinding

class MemoRVAdapter (var memoList : ArrayList<GetTradeListResult>):
    RecyclerView.Adapter<MemoRVAdapter.MyViewHolder>() {

    interface MemoClickListener {
        fun onDeleteClick(item: GetTradeListResult)
    }

    //리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mMemoClickListener : MemoClickListener       //리스너 객체 저장할 변수

    fun setMyItemClickListener(memoClickListener: MemoClickListener){
        mMemoClickListener = memoClickListener                            //리스너 객체를 전달받아서 리스너 객체 변수에 저장
    }

    inner class MyViewHolder(val binding:ItemPossessionCoinMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : GetTradeListResult){
            binding.itemPossessionCoinMemoDateTV.text = item.date.toString()
            binding.itemPossessionCoinMemoCoinNameTV.text = item.coinName
            binding.itemPossessionCoinMemoAverageunitPriceTV.text ="${item.price} 원"
            binding.itemPossessionCoinMemoQuantityTV.text ="${item.amount} 개"
            binding.itemPossessionCoinMemoProfitTV.text = String.format("%.2f", item.price * item.amount)+" 원"
            binding.itemPossessionCoinMemoBuyMemoInputTV.text = item.memo
            if(item.category == "buy"){
                binding.itemPossessionCoinMemoBuyImageIV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoSellImageIV.visibility = View.GONE
                binding.itemPossessionCoinMemoBuyTextTV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoSellTextTV.visibility = View.GONE
            } else{
                binding.itemPossessionCoinMemoBuyImageIV.visibility = View.GONE
                binding.itemPossessionCoinMemoSellImageIV.visibility = View.VISIBLE
                binding.itemPossessionCoinMemoBuyTextTV.visibility = View.GONE
                binding.itemPossessionCoinMemoSellTextTV.visibility = View.VISIBLE
            }

            if(item.isFirst){
                binding.itemPossessionCoinMemoDeleteBtn.visibility = View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPossessionCoinMemoBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(memoList[position])
        holder.binding.itemPossessionCoinMemoDeleteBtn.setOnClickListener { mMemoClickListener.onDeleteClick(memoList[position]) }
    }

    override fun getItemCount(): Int = memoList.size
}