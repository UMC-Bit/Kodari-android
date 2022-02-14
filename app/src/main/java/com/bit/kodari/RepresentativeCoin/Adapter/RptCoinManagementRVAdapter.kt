package com.bit.kodari.Main.Adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.PossessionCoin.RetrofitData.PsnCoinMgtInsquireResult
import com.bit.kodari.RepresentativeCoin.RetrofitData.RptCoinMgtInsquireResult
import com.bit.kodari.databinding.ItemRepresentativeCoinManagementCoinListBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class RptCoinManagementAdapter(var rptCoinList:ArrayList<RptCoinMgtInsquireResult>): RecyclerView.Adapter<RptCoinManagementAdapter.RepresentativeCoinManagementViewHolder>(){
    private var df: DecimalFormat = DecimalFormat("#.##")
    interface MyItemClickListener {
        fun onItemCheck(item: RptCoinMgtInsquireResult)
        fun onItemUnCheck(item:RptCoinMgtInsquireResult)
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
            binding.itemRepresentativeCoinManagementCoinListBitfinexPriceTV.text = formatPrice(item.binancePrice) +"원"
            binding.itemRepresentativeCoinManagementCoinUpbitPriceTV.text = formatPrice(item.upbitPrice) +"원"
            binding.itemRepresentativeCoinManagementCoinListKimchiPremiumTV.text = formatD(item.kimchi) +"%"

            //업비트, 바이낸스 가격 추가하기.


            //클릭했을때 체크 활성화하기.
            if(item.isChecked){
                binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.VISIBLE
                binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility = View.GONE
            } else{
                binding.itemRepresentativeCoinManagementCoinListSelectOffIV.visibility = View.VISIBLE
                binding.itemRepresentativeCoinManagementCoinListSelectOnIV.visibility = View.GONE
            }

        }
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
    public fun formatD(number:Double): String {
        return df.format(number)
    }
    fun formatPrice(number: Double): String{
        lateinit var price: String
        if(number>=1 && number < 10){
            price = String.format("%.2f", number)
        }else if(number>=10 && number < 100){
            price = String.format("%.1f", number)
        }else if(number>=100){
            val price_ = String.format("%.0f", number).toDouble()
            price = formatD(price_)
        }else{
            price = String.format("%.5f", number)
        }
        return price;
    }
}