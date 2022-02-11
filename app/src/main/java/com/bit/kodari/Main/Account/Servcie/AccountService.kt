package com.bit.kodari.Main.Account.Servcie

import android.util.Log
import com.bit.kodari.Main.Account.Data.*
import com.bit.kodari.Main.Account.Retrofit.AccountRetrofitInterface
import com.bit.kodari.Main.Account.Retrofit.ModifyDialogView
import com.bit.kodari.Util.getJwt
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountService {
    private lateinit var modifyDialogView: ModifyDialogView

    fun setModifyDialogView(modifyDialogView: ModifyDialogView) {
        this.modifyDialogView = modifyDialogView
    }

    fun modifyAccountName(accountIdx: Int, modifyAccountNameRequest: ModifyAccountNameRequest) {
        val accountService = getRetorfit().create(AccountRetrofitInterface::class.java)
        accountService.modifyAccountName(getJwt()!!, accountIdx, modifyAccountNameRequest)
            .enqueue(object : Callback<ModifyAccountNameResponse> {
                override fun onResponse(
                    call: Call<ModifyAccountNameResponse>,
                    response: Response<ModifyAccountNameResponse>
                ) {
                    when (response.body()!!.code) {
                        1000 -> {
                            modifyDialogView.modifyAccountNameSuccess(response.body()!!)
                        }
                        else -> {
                            modifyDialogView.modifyAccountNameFailure(response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ModifyAccountNameResponse>, t: Throwable) {
                    Log.d("modifyAccountName", t.toString())
                }
            })
    }

    fun modifyProperty(accountIdx: Int, modifyPropertyRequest: ModifyPropertyRequest) {
        val accountService = getRetorfit().create(AccountRetrofitInterface::class.java)
        accountService.modifyProperty(getJwt()!!, accountIdx, modifyPropertyRequest)
            .enqueue(object : Callback<ModifyPropertyResponse> {
                override fun onResponse(
                    call: Call<ModifyPropertyResponse>,
                    response: Response<ModifyPropertyResponse>
                ) {
                    when (response.body()!!.code) {
                        1000 -> {
                            modifyDialogView.modifyPropertySuccess(response.body()!!)
                        }
                        else -> {
                            modifyDialogView.modifyPropertyFailure(response.body()!!.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ModifyPropertyResponse>, t: Throwable) {
                    Log.d("modifyProperty" , t.toString())
                }
            })
    }

    fun deletePort(portIdx:Int){
        val accountService = getRetorfit().create(AccountRetrofitInterface::class.java)
        accountService.deletePort(getJwt()!!, portIdx).enqueue(object : Callback<DeletePortResponse>{
            override fun onResponse(
                call: Call<DeletePortResponse>,
                response: Response<DeletePortResponse>
            ) {
                when(response.body()!!.code){
                  1000 -> {
                      modifyDialogView.deletePortSuccess(response.body()!!)
                  }
                  else -> {
                      modifyDialogView.deletePortFailure(response.body()!!.message)
                  }
                }
            }

            override fun onFailure(call: Call<DeletePortResponse>, t: Throwable) {
                Log.d("deletePort" , t.toString())
            }
        })
    }
}