package com.bit.kodari.PossessionCoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.kodari.R

class coinData(val coinImage:Int, val coinName:String, val coinSymbol:String, val presentPrice:String, val profit:String, val averagePrice:String, val select:Int)

class PossessionCoinManagementViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
    val coinImage = itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_image_IV)
    val coinName = itemView.findViewById<TextView>(R.id.item_possession_coin_management_coin_list_coin_name_TV)
    val coinSymbol = itemView.findViewById<TextView>(R.id.item_possession_coin_management_coin_list_coin_symbol_TV)
    val presentPrice = itemView.findViewById<TextView>(R.id.item_possession_coin_management_coin_list_price_TV)
    val profit = itemView.findViewById<TextView>(R.id.item_possession_coin_management_coin_list_profit_plus_TV)
    val averagePrice = itemView.findViewById<TextView>(R.id.item_possession_coin_management_coin_list_averageunit_price_TV)
    val select = itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_off_IV)


    init {
        itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_off_IV).setOnClickListener {
            itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_off_IV).visibility =
                View.GONE
            itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_on_IV).visibility =
                View.VISIBLE
        }

        itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_on_IV).setOnClickListener {
            itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_on_IV).visibility =
                View.GONE
            itemView.findViewById<ImageView>(R.id.item_possession_coin_management_coin_list_select_off_IV).visibility =
                View.VISIBLE
        }
    }
}

class PossessionCoinManagementAdapter(val coinList:ArrayList<coinData>): RecyclerView.Adapter<PossessionCoinManagementViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PossessionCoinManagementViewHolder {

        val cellForRow=LayoutInflater.from(parent.context).inflate(R.layout.item_possession_coin_management_coin_list, parent, false)

        return PossessionCoinManagementViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: PossessionCoinManagementViewHolder, position: Int) {
        holder.coinImage.setImageResource(coinList[position].coinImage)
        holder.coinName.text = coinList[position].coinName
        holder.coinSymbol.text = coinList[position].coinSymbol
        holder.presentPrice.text = coinList[position].presentPrice
        holder.profit.text = coinList[position].profit
        holder.averagePrice.text = coinList[position].averagePrice
        holder.select.setImageResource(coinList[position].select)
    }

    override fun getItemCount()=coinList.size
}