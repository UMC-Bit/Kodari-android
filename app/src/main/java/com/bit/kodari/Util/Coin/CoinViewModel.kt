package com.bit.kodari.Util.Coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bit.kodari.Main.Data.PossesionCoinResult
import com.bit.kodari.Main.Data.RepresentCoinResult

class CoinViewModel(userCoinList: List<PossesionCoinResult>?, representCoinList: List<RepresentCoinResult>?): ViewModel() {
    private var userCoinList = MutableLiveData<List<PossesionCoinResult>>()
    private var representCoinList = MutableLiveData<List<RepresentCoinResult>>()
    private var userCoinPosition = 0;
    private var representcoinPosition = 0;
    init{
        this.userCoinList.value = userCoinList
        this.representCoinList.value = representCoinList
    }
    val userCoinData : LiveData<List<PossesionCoinResult>>
        get() = userCoinList
    val representCoinData : LiveData<List<RepresentCoinResult>>
        get() = representCoinList

    fun getUpdateUserCoin(userCoinList: List<PossesionCoinResult>, position: Int){
        this.userCoinList.value = userCoinList
        userCoinPosition = position
    }
    fun getUpdateRepresentCoin(representCoinList: List<RepresentCoinResult>, position: Int){
        this.representCoinList.value = representCoinList
        representcoinPosition = position
    }
    fun getUserCoinPosition(): Int{
        return userCoinPosition
    }
    fun getRepresentCoinPosition(): Int{
        return representcoinPosition
    }
}