package com.bit.kodari.Util.Coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

class CoinViewModel(userCoinList: List<PossesionCoinResult>?, representCoinList: List<RepresentCoinResult>?): ViewModel() {
    private var userCoinList = MutableLiveData<List<PossesionCoinResult>>()
    private var representCoinList = MutableLiveData<List<RepresentCoinResult>>()
    init{
        this.userCoinList.value = userCoinList
        this.representCoinList.value = representCoinList
    }
    val userCoinData : LiveData<List<PossesionCoinResult>>
        get() = userCoinList
    val representCoinData : LiveData<List<RepresentCoinResult>>
        get() = representCoinList

    fun getUpdateUserCoin(userCoinList: List<PossesionCoinResult>){
        this.userCoinList.value = userCoinList
    }
    fun getUpdateRepresentCoin(representCoinList: List<RepresentCoinResult>){
        this.representCoinList.value = representCoinList
    }
}