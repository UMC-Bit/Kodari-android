package com.bit.kodari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bit.kodari.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.login_container_fl , FragmentLogin()).commitAllowingStateLoss()
    }
}