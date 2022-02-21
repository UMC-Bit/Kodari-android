package com.bit.kodari.Util

import android.graphics.Color
import java.text.DecimalFormat

private var df: DecimalFormat = DecimalFormat("#,###.##")

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
    }else if(number<=-1 && number>-10){
        price = String.format("%.2f", number)
    }else if(number<=-10 && number>-100){
        price = String.format("%.1f", number)
    }else if(number <=-100){
        val price_ = String.format("%.0f", number).toDouble()
        price = formatD(price_)
    }else if(number == 0.0){
        price = String.format("%.0f", number)
    }else{
        price = String.format("%.5f", number)
    }
    return price;
}

fun getPriceColor(change: Double): Int{
    // 상승, 하락, 보합
    var colorUpbit = 0 // 업비트
    if(change == 1.0){
        colorUpbit = Color.RED
    }else if(change == -1.0){
        colorUpbit = Color.BLUE
    }else{
        colorUpbit = Color.BLACK
    }
    return colorUpbit
}