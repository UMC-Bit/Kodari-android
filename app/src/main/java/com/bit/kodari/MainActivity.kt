package com.bit.kodari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bit.kodari.databinding.ActivityMainBinding
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()


        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, DebateMineFragment())
            .commitAllowingStateLoss()

    }

    fun setListener(){
        binding.mainBottomnavigationBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.myCoin -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.debate -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, DebateMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.feed -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, SearchFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
                }

                R.id.add_on -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, LockerFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
                }
                R.id.profile -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, LockerFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

}
