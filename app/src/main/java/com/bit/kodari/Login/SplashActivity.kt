package com.bit.kodari.Login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.bit.kodari.Config.BaseActivity
import com.bit.kodari.Intro.IntroActivity
import com.bit.kodari.Login.Retrofit.LogInView
import com.bit.kodari.Login.RetrofitData.LogInInfo
import com.bit.kodari.Login.RetrofitData.LogInResponse
import com.bit.kodari.Login.Service.LogInService
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.R
import com.bit.kodari.Util.getAutoLogin
import com.bit.kodari.Util.getEmail
import com.bit.kodari.Util.getPw
import com.bit.kodari.Util.saveLoginInfo
import com.bit.kodari.databinding.ActivitySplashBinding
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) ,LogInView {
    var anim_fade_in_1: Animation? = null
    var anim_fade_in_2: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window!!.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        super.onCreate(savedInstanceState)
    }

    override fun initAfterBinding() {
        var logo : ImageView = findViewById(R.id.splash_logo_iv)
        var title : TextView = findViewById(R.id.splash_logo_tv)
        var copyright : TextView = findViewById(R.id.splash_name_tv)

        anim_fade_in_1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_1)
        anim_fade_in_2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_2)
        title.startAnimation(anim_fade_in_2)
        copyright.startAnimation(anim_fade_in_2)
        logo.startAnimation(anim_fade_in_1)

        Handler(Looper.getMainLooper()).postDelayed({
            if(getAutoLogin()){             //?????? ????????? ????????? ?????????
                if(getEmail() == null || getPw() == null){          //????????? ???????????? ??????????????? ?????????
                    startNextActivity(IntroActivity::class.java)
                    finish()
                } else if(getEmail() != null && getPw() != null){                                             //????????? ????????? ?????????
                    val loginInfo = LogInInfo(getEmail()!! , getPw()!!)
                    val logInService = LogInService()
                    logInService.setLogInView(this)
                    logInService.getLogIn(loginInfo)            //????????? ????????? ????????? ??????
                }
            } else{                        //?????? ????????? ????????? ??????????????????
                startNextActivity(IntroActivity::class.java)
                finish()
            }

        }, 2000)
    }

    //????????? ??????????????? , ??? , ?????? ????????????
    override fun logInSuccess(response: LogInResponse) {
        when(response.code){
          1000 -> {         //????????? ???????????????
              val jwt = response.result.jwt
              val userIdx = response.result.userIdx
              saveLoginInfo(jwt , getEmail()!!, getPw()!!,userIdx)
              Log.d("jwt" , "${jwt} ??? ${userIdx}")
              startNextActivity(MainActivity::class.java)
              finish()
          }
          else -> {     //????????? ???????????? ???
              startActivity(Intent(this, LoginActivity::class.java))
              finish()
          }
        }
    }

    //????????? ???????????????
    override fun logInFailure(message: String) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}