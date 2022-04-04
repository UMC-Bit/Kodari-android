package com.bit.kodari.Main

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.bit.kodari.AddOn.AddOnAlarmMainFragment
import com.bit.kodari.AddOn.AddOnMainFragment
import com.bit.kodari.databinding.ActivityMainBinding
import com.bit.kodari.Debate.DebateMainFragment
import com.bit.kodari.Feed.FeedMainFragment
import com.bit.kodari.Main.RetrofitInterface.MainView
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Profile.ProfileMainFragment
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.R
import com.bit.kodari.Util.getEmail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationItemView


class MainActivity : AppCompatActivity() , MainView {
    private lateinit var binding : ActivityMainBinding
    private var isImageLoaded = false
    private var backKeyPressedTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()

        supportFragmentManager.beginTransaction().replace(R.id.main_container_fl, HomeFragment())
            .commit()



    }

    override fun onStart() {
        super.onStart()
        //바텀 네비게이션 뷰 프로필 이미지 셋팅
        //회원 목록 이메일로 조회해서 프로필 url 가져와야할 것 같음.
        callUserFromEmail(getEmail()!!)
    }

    fun setListener(){
        binding.mainBottomnavigationBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.myCoin -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, HomeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.debate -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, DebateMainFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, FeedMainFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.add_on -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, AddOnAlarmMainFragment())
//                        .commit()
//                    return@setOnItemSelectedListener true
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, FeedMainFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container_fl, ProfileMainFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

    //왜 되는거지 ? 뭔지 모르지만 됐음 .. ->해결 나중에 분석 필요
    fun loadImage(
        imageUrl: String?,
        @IdRes itemId: Int,
        @DrawableRes placeHolderResourceId: Int,
        @IdRes fragmentNavigationId: Int = 0
    ) {

        val navigationItemView = findViewById<BottomNavigationItemView>(itemId)

        val imageView = navigationItemView.children.find {
            it is ImageView
        } as? ImageView ?: return

        loadProfileImage(imageView, imageUrl, placeHolderResourceId, itemId)

    }


    private fun loadProfileImage(
        imageView: ImageView,
        imageUrl: String?,
        @DrawableRes placeHolderResourceId: Int,
        @IdRes itemId: Int
    ) {

        Glide.with(this)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .optionalCircleCrop()
            .placeholder(placeHolderResourceId)
            .error(R.drawable.ic_basic_profile)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    isImageLoaded = true
                    return false
                }

            })
            .into(imageView)
    }


    override fun getUserSuccess(resp: GetProfileResponse) {
        Log.d("url","${resp.result[0].profileImgUrl}" )
        loadImage(resp.result[0].profileImgUrl ,R.id.profile , R.drawable.ic_basic_profile)
        //loadProfileImage(resp.result[0].profileImgUrl)
    }

    override fun getUserFailure(message: String) {
        Log.d("url" , message)
    }

    fun callUserFromEmail(email:String){
        val homeService = HomeService()
        homeService.setMainView(this)
        homeService.getUserFromEmail(email)
    }

}
