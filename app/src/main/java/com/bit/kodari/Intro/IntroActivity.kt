package com.bit.kodari.Intro
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bit.kodari.R
import com.bit.kodari.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroBinding
    private lateinit var introViewpagerAdapter : IntroViewpagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        introViewpagerAdapter = IntroViewpagerAdapter(this)
        binding.introVp.adapter = introViewpagerAdapter
        binding.introVp.offscreenPageLimit = 4

    }

}