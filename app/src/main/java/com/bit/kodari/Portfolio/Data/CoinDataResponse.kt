package com.bit.kodari.Portfolio.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CoinDataResponse(var coinIdx: Int,           //소유
                       var coinImg: String,
                       var coinName: String,
                       var symbol: String,
                       var userIdx:Int,                 //소유
                       var accountIdx:Int,              //소유
                       var priceAvg:String,             //소유
                       var amount:String) : Serializable    //소유