package com.bit.kodari.Login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bit.kodari.Config.BaseActivity
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

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) ,LogInView {
    var anim_fade_in_1: Animation? = null
    var anim_fade_in_2: Animation? = null

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
            if(getAutoLogin()){             //자동 로그인 활성화 됐을때
                if(getEmail() == null || getPw() == null){          //저장된 아이디와 패스워드가 없으면
                    startNextActivity(LoginActivity::class.java)
                    finish()
                } else if(getEmail() != null && getPw() != null){                                             //로그인 정보가 있으면
                    val loginInfo = LogInInfo(getEmail()!! , getPw()!!)
                    val logInService = LogInService()
                    logInService.setLogInView(this)
                    logInService.getLogIn(loginInfo)            //로그인 정보로 로그인 시도
                }
            } else{                        //자동 로그인 활성화 안되어있으면
                startNextActivity(LoginActivity::class.java)
                finish()
            }

        }, 2000)
    }

    //로그인 성공했을때 , 단 , 코드 나눠야함
    override fun logInSuccess(response: LogInResponse) {
        when(response.code){
          1000 -> {         //로그인 성공했을때
              val jwt = response.result.jwt
              val userIdx = response.result.userIdx
              saveLoginInfo(jwt , getEmail()!!, getPw()!!,userIdx)
              Log.d("jwt" , "${jwt} 와 ${userIdx}")
              startNextActivity(MainActivity::class.java)
              finish()
          }
          else -> {     //로그인 실패했을 때
              startActivity(Intent(this, LoginActivity::class.java))
              finish()
          }
        }
    }

    //로그인 실패했을때
    override fun logInFailure(message: String) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}