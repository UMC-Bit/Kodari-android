package com.bit.kodari.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bit.kodari.AddOn.AddOnMainFragment
import com.bit.kodari.databinding.ActivityMainBinding
import com.bit.kodari.Debate.DebateMainFragment
import com.bit.kodari.Feed.FeedMainFragment
import com.bit.kodari.Profile.ProfileMainFragment
import com.bit.kodari.R
import com.bit.kodari.Util.Upbit.UpbitService


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()

        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, HomeFragment())
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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, FeedMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.add_on -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, AddOnMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, ProfileMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

}
