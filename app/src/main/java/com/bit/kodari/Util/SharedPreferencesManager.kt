package com.bit.kodari.Util

import com.MyApplicationClass.Companion.EMAIL
import com.MyApplicationClass.Companion.PASSWORD
import com.MyApplicationClass.Companion.USER_IDX
import com.MyApplicationClass.Companion.X_ACCESS_TOKEN
import com.MyApplicationClass.Companion.mSharedPreferences

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_ACCESS_TOKEN, jwtToken)

    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString(X_ACCESS_TOKEN, null)


fun saveLoginInfo(jwtToken: String , email: String , pw:String , userIdx:Int){
    val editor = mSharedPreferences.edit()
    editor.putString(X_ACCESS_TOKEN, jwtToken)
    editor.putString(EMAIL , email)
    editor.putString(PASSWORD,pw)
    editor.putInt(USER_IDX , userIdx)

    editor.apply()

}

fun getEmail() :String? = mSharedPreferences.getString(EMAIL,null)

fun getPw() : String? = mSharedPreferences.getString(PASSWORD , null)

fun getUserIdx() : Int = mSharedPreferences.getInt(USER_IDX, 0)