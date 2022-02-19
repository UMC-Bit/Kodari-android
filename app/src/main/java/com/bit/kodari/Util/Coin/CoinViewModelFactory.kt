package com.bit.kodari.Util.Coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

class CoinViewModelFactory(private val userCoinList: List<PossesionCoinResult>?, private val representCoinList:
ArrayList<RepresentCoinResult>?
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CoinViewModel::class.java)){
            return CoinViewModel(userCoinList, representCoinList) as T
        }
        throw IllegalAccessException("Un")
    }

}