package com.bit.kodari.Main.Account.Retrofit

import com.bit.kodari.Main.Account.Data.DeletePortResponse
import com.bit.kodari.Main.Account.Data.ModifyAccountNameResponse
import com.bit.kodari.Main.Account.Data.ModifyPropertyResponse

interface ModifyDialogView {
    fun modifyAccountNameSuccess(response:ModifyAccountNameResponse)
    fun modifyAccountNameFailure(message:String)
    fun modifyPropertySuccess(response:ModifyPropertyResponse)
    fun modifyPropertyFailure(message:String)
    fun deletePortSuccess(response: DeletePortResponse)
    fun deletePortFailure(message:String)

}