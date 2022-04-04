package com

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MyApplicationClass : Application() {
    companion object{
        const val X_ACCESS_TOKEN: String = "X-ACCESS-TOKEN"         // JWT Token Key
        const val EMAIL : String = "E_MAIL"
        const val PASSWORD: String = "PASSWORD"
        const val USER_IDX :String = "USER_IDX"
        const val TAG: String = "TEMPLATE-APP"                      // Log, SharedPreference
        const val AUTO_LOGIN : String = "AUTO_LOGIN"

        lateinit var mSharedPreferences: SharedPreferences
        var myAccountIdx = 0                                        //기본 값으로 셋팅.
        var myPortIdx = 0
        var pageIdx = 0
        var marketName = ""
    }

    override fun onCreate() {
        super.onCreate()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        try {
            val config = AmplifyConfiguration.builder(applicationContext).devMenuEnabled(false).build()
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(config, applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }
}