package com.bit.kodari.Portfolio.Retrofit

import com.bit.kodari.Portfolio.Data.SearchCoinResponse

interface SearchCoinView {
    fun getSearchCoinAllSuccess(response: SearchCoinResponse)
    fun getSearchCoinAllFailure(message:String)
}