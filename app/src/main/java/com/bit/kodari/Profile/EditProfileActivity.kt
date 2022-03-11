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
import com.bit.kodari.BuildConfig
import com.bit.kodari.Config.BaseActivity
import com.bit.kodari.Main.Service.HomeService
import com.bit.kodari.Util.getEmail
import com.bit.kodari.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

//편집 창 왔을때 유저 정보 호출해야함
class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding>(ActivityEditProfileBinding::inflate), ProfileEditView {

    private lateinit var nickName: String
    private lateinit var email:String
    private lateinit var url:String

    //private var imageView: ImageView? = binding.editProfileMainImageIv
    private var currentImageUri: Uri? = null               //가져온 이미지 uri
    private var file: File? = null                          //null이면 실행 X
    private var uploadFileName: String? = null

    private var chkNickName = true
    private var chkProfile = true

    //갤러리에서 사진 제대로 가져왔을 경우 실행되는 메서드.
    private val pickerActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                currentImageUri = it.data?.data         //이미지를 uri 로 가져오기
                try {
                    currentImageUri?.let {
                        //데이터 무사히 가져왔을 경우
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
                    Log.d("pcik", "실패 : ${e}")
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
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
        //Intent로 변경해야함.

        if (intent.hasExtra("nickName")) {
            nickName = intent.getStringExtra("nickName")!!
            binding.editProfileInputNicknameEt.setText(nickName)
        }
        //프로필 , 이메일 셋팅..
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
                showToast("닉네임 변경 성공")
                if(chkNickName && chkProfile){          //둘다 true면 변경
//                    (context as MainActivity).supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_container_fl, ProfileMainFragment())
//                        .commitAllowingStateLoss()
                    finish()        //둘다 true면 종료
                }

            }
            else -> {
                showToast("닉네임 변경 실패")
            }
        }

    }

    override fun updateNameFailure(message: String) {
        showToast(message)
    }

    //프로필 이미지 Update 성공
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

    //프로필 이미지 update 실패
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
            if (nickName != this.nickName) {      //처음 입력된거와 다르면실행
                chkNickName = false
                callUpdateNickname(nickName)
            }

            if (file != null) {                   //이미지 변경했으면
                //showToast(file!!.absolutePath)
                chkProfile = false
                uploadWithTransferUtility(file!!.name, file!!)         //파일 업로드하기 , 제대로 업로드 됐으면 실행.
            }


            //S3에 이미지 올리고 url 받아서 서버에등록.
        }

        binding.editProfileDoubleCheckB.setOnClickListener {
            val profileService = ProfileService()
            profileService.setProfileEditView(this)
            profileService.getCheckNickname(NicknameInfo(binding.editProfileInputNicknameEt.text.toString()))
        }

        binding.editProfileChangeTv.setOnClickListener {
            //눌렀을 때 프로필 이미지로 할 사진 가져오기 , 이미지뷰에 적용하기 , ->완료 누르면 이미지 S3에 올리고 url 리턴 받은뒤 서버에 올리기
            //갤러리 접근 권한 먼저 체크
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 권한이 잘 부여 되었을 때 갤러리에서 사진을 선택하는 기능
                    Log.d("pcik", "실행 1")
                    navigatesPhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 교육용 팝 확인 후 권한 팝업 띄우는 기능
                    showContextPopupPermission()
                }
                else -> {
                    Log.d("pcik", "실행 2")
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000
                    )
                }
            }
        }
    }

    //권한 요청 팝업 띄위기 , 권한 확인하면 naviagatesPhotos 실행
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showContextPopupPermission() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
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
        showToast("Nickname Check 실패 ,$message")
    }


    fun callUpdateNickname(nickName: String) {
        val profileService = ProfileService()
        profileService.setProfileEditView(this)
        showLoadingDialog(this)
        profileService.updateName(nickName)
    }

    private fun callUpdateImage(){
        val url = "https://kodari-s3.s3.ap-northeast-2.amazonaws.com/"+uploadFileName       //Url 저장
        val profileService = ProfileService()
        Log.d("updateImage" , url)
        profileService.setProfileEditView(this)
        showLoadingDialog(this)
        profileService.updateProfileImg(url)
    }

    //S3에 이미지 업로드하기 , 코드 분석하기
    fun uploadWithTransferUtility(fileName: String, file: File) {
        val awsCredentials =
            BasicAWSCredentials("${BuildConfig.S3_API_ACCESS_KEY}", "${BuildConfig.S3_API_SECRET_KEY}")
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(
            this.applicationContext
        ).build()
        TransferNetworkLossHandler.getInstance(this.applicationContext)

        uploadFileName = "profile/" + UUID.randomUUID()
            .toString() + fileName           //저장할 File이름 저장 -> 이걸 이용해서 url 만들거임

        val uploadObserver = transferUtility.upload(
            "kodari-s3",
            uploadFileName,
            file, CannedAccessControlList.PublicRead
        ) // (bucket api, file이름, file객체)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state === TransferState.COMPLETED) {
                    // Handle a completed upload
                    Log.d("MYTAG", "업로드 성공 파일 이름 : ${fileName}")
                    callUpdateImage()                   //업로드 성공했으면 갱신
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (current.toDouble() / total * 100.0).toInt()
                Log.d("MYTAG", "UPLOAD - - ID: \$id, percent done = \$done")
            }

            override fun onError(id: Int, ex: java.lang.Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: \$id - - EX:$ex")
            }
        })
    }

    //uri 로 전역변수 file 에 file 만들기
    fun getPathFromUri() {
        var cursor: Cursor? = null
//                            *
//                            *  Uri 스키마를
//                            *  content:/// 에서 file:/// 로  변경한다.
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


    //권한 요청 처리 결과 수신
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
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
    }


}