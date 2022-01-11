package com.bit.kodari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bit.kodari.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, HomeFragment())
            .commitAllowingStateLoss()

    }
}