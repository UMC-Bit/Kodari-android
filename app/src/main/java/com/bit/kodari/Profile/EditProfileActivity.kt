package com.bit.kodari.Profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.bit.kodari.Config.BaseFragment
import com.bit.kodari.Login.LoginActivity
import com.bit.kodari.Login.RetrofitData.NicknameInfo
import com.bit.kodari.Login.RetrofitData.NicknameResponse
import com.bit.kodari.Login.Service.ProfileService
import com.bit.kodari.Login.SignupPwFragment
import com.bit.kodari.Main.MainActivity
import com.bit.kodari.Profile.Retrofit.ProfileEditView
import com.bit.kodari.Profile.RetrofitData.GetProfileResponse
import com.bit.kodari.Profile.RetrofitData.UpdateNameResponse
import com.bit.kodari.Profile.RetrofitData.UpdateProfileImgResponse
import com.bit.kodari.R
import java.io.File

import com.amazonaws.regions.Regions

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amplifyframework.core.Amplify
import com.bit.kodari.BuildConfig
import com.bit.kodari.Config.BaseActivity
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Util.getEmail
import com.bit.kodari.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

//?????? ??? ????????? ?????? ?????? ???????????????
class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding>(ActivityEditProfileBinding::inflate), ProfileEditView {

    private lateinit var nickName: String
    private lateinit var email:String
    private lateinit var url:String

    //private var imageView: ImageView? = binding.editProfileMainImageIv
    private var currentImageUri: Uri? = null               //????????? ????????? uri
    private var file: File? = null                          //null?????? ?????? X
    private var uploadFileName: String? = null

    private var chkNickName = true
    private var chkProfile = true

    //??????????????? ?????? ????????? ???????????? ?????? ???????????? ?????????.
    private val pickerActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                currentImageUri = it.data?.data         //???????????? uri ??? ????????????
                try {
                    currentImageUri?.let {
                        //????????? ????????? ???????????? ??????
                        if (Build.VERSION.SDK_INT < 28) {
                            getPathFromUri()
                            binding.editProfileMainImageIv.setImageURI(currentImageUri)
                        } else {
                            getPathFromUri()
                            binding.editProfileMainImageIv.setImageURI(currentImageUri)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("pcik", "?????? : ${e}")
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initAfterBinding() {
        setInit()
        setListener()
    }

    fun setInit() {
        //Intent??? ???????????????.

        if (intent.hasExtra("nickName")) {
            nickName = intent.getStringExtra("nickName")!!
            binding.editProfileInputNicknameEt.setText(nickName)
        }
        //????????? , ????????? ??????..
        if(intent.hasExtra("email")){
            email = intent.getStringExtra("email")!!
            binding.editProfileEmailAddressTv.text = email
        }

        if(intent.hasExtra("url") && intent.getStringExtra("url") != null){
            url = intent.getStringExtra("url")!!
            Glide.with(binding.editProfileMainImageIv)
                .load(url)
                .placeholder(R.drawable.ic_basic_profile)
                .error(R.drawable.ic_basic_profile)
                .into(binding.editProfileMainImageIv)
        }else{
            Glide.with(binding.editProfileMainImageIv)
                .load(R.drawable.ic_basic_profile)
                .placeholder(R.drawable.ic_basic_profile)
                .error(R.drawable.ic_basic_profile)
                .into(binding.editProfileMainImageIv)
        }

    }

    override fun updateNameSuccess(resp: UpdateNameResponse) {
        dismissLoadingDialog()
        chkNickName = true
        when (resp.code) {
            1000 -> {
                showToast("????????? ?????? ??????")
                if(chkNickName && chkProfile){          //?????? true??? ??????
//                    (context as MainActivity).supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, ProfileMainFragment())
//                        .commitAllowingStateLoss()
                    finish()        //?????? true??? ??????
                }

            }
            else -> {
                showToast("????????? ?????? ??????")
            }
        }

    }

    override fun updateNameFailure(message: String) {
        showToast(message)
    }

    //????????? ????????? Update ??????
    override fun updateProfileImgSuccess(resp: UpdateProfileImgResponse) {
        dismissLoadingDialog()
        chkProfile = true
        if(chkProfile && chkNickName){
//            requireActivity().supportFragmentManager.beginTransaction().
//            replace(R.id.main_container_fl , ProfileMainFragment()).commit()
            finish()

        }
        Log.d("updateImage" , "${resp.result}")
    }

    //????????? ????????? update ??????
    override fun updateProfileImgFailure(message: String) {
        dismissLoadingDialog()
        showToast(message)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setListener() {
        binding.editProfilePreIv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container_fl, ProfileMainFragment()).commitAllowingStateLoss()
            finish()
        }

        binding.editProfileFinishB.setOnClickListener {
            val nickName = binding.editProfileInputNicknameEt.text.toString()
            if (nickName != this.nickName) {      //?????? ??????????????? ???????????????
                chkNickName = false
                callUpdateNickname(nickName)
            }

            if (file != null) {                   //????????? ???????????????
                //showToast(file!!.absolutePath)
                chkProfile = false
                uploadWithTransferUtility(file!!.name, file!!)         //?????? ??????????????? , ????????? ????????? ????????? ??????.
            }


            //S3??? ????????? ????????? url ????????? ???????????????.
        }

        binding.editProfileDoubleCheckB.setOnClickListener {
            val profileService = ProfileService()
            profileService.setProfileEditView(this)
            profileService.getCheckNickname(NicknameInfo(binding.editProfileInputNicknameEt.text.toString()))
        }

        binding.editProfileChangeTv.setOnClickListener {
            //????????? ??? ????????? ???????????? ??? ?????? ???????????? , ??????????????? ???????????? , ->?????? ????????? ????????? S3??? ????????? url ?????? ????????? ????????? ?????????
            //????????? ?????? ?????? ?????? ??????
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // ????????? ??? ?????? ????????? ??? ??????????????? ????????? ???????????? ??????
                    Log.d("pcik", "?????? 1")
                    navigatesPhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // ????????? ??? ?????? ??? ?????? ?????? ????????? ??????
                    showContextPopupPermission()
                }
                else -> {
                    Log.d("pcik", "?????? 2")
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000
                    )
                }
            }
        }
    }

    //?????? ?????? ?????? ????????? , ?????? ???????????? naviagatesPhotos ??????
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showContextPopupPermission() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ????????? ???????????? ????????? ?????? ????????? ???????????????.")
            .setPositiveButton("????????????") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("????????????") { _, _ -> }
            .create()
            .show()
    }

    private fun navigatesPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        pickerActivityLauncher.launch(intent)
    }


    override fun getCheckNicknameSuccess(response: NicknameResponse) {
        when (response.code) {
            1000 -> {
                binding.editProfileErrorTv.text = response.result
            }
            else -> {
                binding.editProfileErrorTv.text = response.message
            }
        }
    }

    override fun getCheckNicknameFailure(message: String) {
        showToast("Nickname Check ?????? ,$message")
    }


    fun callUpdateNickname(nickName: String) {
        val profileService = ProfileService()
        profileService.setProfileEditView(this)
        showLoadingDialog(this)
        profileService.updateName(nickName)
    }

    private fun callUpdateImage(){
        val url = "https://kodari-s3.s3.ap-northeast-2.amazonaws.com/public/"+uploadFileName       //Url ??????
        val profileService = ProfileService()
        Log.d("updateImage" , url)
        profileService.setProfileEditView(this)
        showLoadingDialog(this)
        profileService.updateProfileImg(url)
    }

    //S3??? ????????? ??????????????? , ?????? ????????????
    fun uploadWithTransferUtility(fileName: String, file: File) {
//        val awsCredentials =
//            BasicAWSCredentials("${BuildConfig.S3_API_ACCESS_KEY}", "${BuildConfig.S3_API_SECRET_KEY}")
//        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))
//
//        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(
//            this.applicationContext
//        ).build()
//        TransferNetworkLossHandler.getInstance(this.applicationContext)

        uploadFileName = "profile/" + UUID.randomUUID()
            .toString() + fileName           //????????? File?????? ?????? -> ?????? ???????????? url ????????????

        Amplify.Storage.uploadFile(
            uploadFileName!!,
            file,
            { result ->
                Log.d("MyAmplifyApp", "Successfully uploaded: " + result)
                Log.d("MyAmplifyApp", "????????? ?????? ?????? ?????? : ${fileName} , ????????? ?????? ?????? : ${uploadFileName}")
                callUpdateImage()
            },
            { error -> Log.d("MyAmplifyApp", "Upload failed", error) }
        )

//
//        val uploadObserver = transferUtility.upload(
//            "kodari-s3",
//            uploadFileName,
//            file, CannedAccessControlList.PublicRead
//        ) // (bucket api, file??????, file??????)
//
//        uploadObserver.setTransferListener(object : TransferListener {
//            override fun onStateChanged(id: Int, state: TransferState) {
//                if (state === TransferState.COMPLETED) {
//                    // Handle a completed upload
//                    Log.d("MYTAG", "????????? ?????? ?????? ?????? : ${fileName}")
//                    callUpdateImage()                   //????????? ??????????????? ??????
//                }
//            }
//
//            override fun onProgressChanged(id: Int, current: Long, total: Long) {
//                val done = (current.toDouble() / total * 100.0).toInt()
//                Log.d("MYTAG", "UPLOAD - - ID: \$id, percent done = \$done")
//            }
//
//            override fun onError(id: Int, ex: java.lang.Exception) {
//                Log.d("MYTAG", "UPLOAD ERROR - - ID: \$id - - EX:$ex")
//            }
//        })
    }

    //uri ??? ???????????? file ??? file ?????????
    fun getPathFromUri() {
        var cursor: Cursor? = null
//                            *
//                            *  Uri ????????????
//                            *  content:/// ?????? file:/// ???  ????????????.
//                            */
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            assert(currentImageUri != null)
            cursor =
                this.getContentResolver()
                    .query(currentImageUri!!, proj, null, null, null)
            Log.d("pick", "cursor: ${cursor}")
            assert(cursor != null)
            val column_index =
                cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            Log.d("pick", "column_index : ${column_index}")
            cursor?.moveToFirst()
            file = File(cursor?.getString(column_index!!))
            Log.d("pick", "file : ${file?.absolutePath}")
        } finally {
            cursor?.close()
        }
    }


    //?????? ?????? ?????? ?????? ??????
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigatesPhotos()
                else
                    Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
    }


}