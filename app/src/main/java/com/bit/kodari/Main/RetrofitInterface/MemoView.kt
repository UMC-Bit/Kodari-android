package com.bit.kodari.Main.RetrofitInterface

import com.bit.kodari.Main.Data.GetTradeListResponse

interface MemoView {        //거래내역 출력하는 뷰
    fun getTradeListSuccess(response:GetTradeListResponse)
    fun getTradeListFailure(message:String)
}