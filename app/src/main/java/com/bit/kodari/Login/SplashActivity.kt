package com.bit.kodari.Login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bit.kodari.R

class SplashActivity : AppCompatActivity() {
    var anim_fade_in_1: Animation? = null
    var anim_fade_in_2: Animation? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var logo : ImageView = findViewById(R.id.splash_logo_iv)
        var title : TextView = findViewById(R.id.splash_logo_tv)
        var copyright : TextView = findViewById(R.id.splash_name_tv)

        anim_fade_in_1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_1)
        anim_fade_in_2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_2)
        title.startAnimation(anim_fade_in_2)
        copyright.startAnimation(anim_fade_in_2)
        logo.startAnimation(anim_fade_in_1)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)

    }
}