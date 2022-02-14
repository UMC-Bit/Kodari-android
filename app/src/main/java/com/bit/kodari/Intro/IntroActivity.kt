package com.bit.kodari.Intro

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bit.kodari.R
import com.bit.kodari.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    companion object{
        var position: Int = 0
    }
    lateinit var binding: ActivityIntroBinding
    lateinit var viewPager: ViewPager2
    public lateinit var indicator0_iv: ImageView
    lateinit var indicator1_iv: ImageView
    lateinit var indicator2_iv: ImageView
    lateinit var indicator3_iv: ImageView
    private lateinit var introViewpagerAdapter: IntroViewpagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        viewPager = binding.introVp
        // viewChangeSetting
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicatorView(position)
            }
        })
        // 인디케이터
        indicator0_iv = binding.introIndicator0Iv
        indicator1_iv = binding.introIndicator1Iv
        indicator2_iv = binding.introIndicator2Iv
        indicator3_iv = binding.introIndicator3Iv
        // ImageView 셋팅
        indicator0_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator1_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator2_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator3_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        setContentView(binding.root)
        introViewpagerAdapter = IntroViewpagerAdapter(this)
        binding.introVp.adapter = introViewpagerAdapter
        binding.introVp.offscreenPageLimit = 4
    }

    fun indicatorView(position: Int) {
        indicator0_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator1_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator2_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        indicator3_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
        when(position){
            0 -> indicator0_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_yellow))
            1 -> indicator1_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_yellow))
            2 -> indicator2_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_yellow))
            3 -> indicator3_iv.setImageDrawable(getDrawable(R.drawable.shape_circle_yellow))
        }
    }
}


