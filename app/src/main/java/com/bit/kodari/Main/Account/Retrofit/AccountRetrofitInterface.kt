package com.bit.kodari.Main.Account.Retrofit

import com.bit.kodari.Main.Account.Data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AccountRetrofitInterface {
    @PATCH("/account/modifyAccountName/{accountIdx}")
    fun modifyAccountName(@Header("X-ACCESS-TOKEN") jwt:String , @Path("accountIdx") accountIdx:Int , @Body modifyAccountNameRequest: ModifyAccountNameRequest) : Call<ModifyAccountNameResponse>

    @PATCH("/account/modifyProperty/{accountIdx}")
    fun modifyProperty(@Header("X-ACCESS-TOKEN") jwt:String , @Path("accountIdx") accountIdx:Int , @Body modifyPropertyRequest: ModifyPropertyRequest) : Call<ModifyPropertyResponse>

    @PATCH("/portfolio/delPortfolio/{portIdx}")
    fun deletePort(@Header("X-ACCESS-TOKEN") jwt:String , @Path("portIdx") portIdx: Int) : Call<DeletePortResponse>
}