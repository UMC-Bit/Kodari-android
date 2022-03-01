package com.bit.kodari.Login.Service

import android.util.Log
import com.bit.kodari.Login.Retrofit.*
import com.bit.kodari.Login.RetrofitData.*
import com.bit.kodari.Util.getRetorfit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInService() {

    private lateinit var signUpView: SignUpView
    private lateinit var logInView: LogInView
    private lateinit var emailView: EmailView
    private lateinit var passwordView: PasswordView
    private lateinit var nicknameView: NicknameView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLogInView(logInView : LogInView){
        this.logInView = logInView
    }

    fun setEmailView(emailView: EmailView) {
        this.emailView = emailView
    }

    fun setPasswordView(passwordView: PasswordView) {
        this.passwordView = passwordView
    }

    fun setNicknameView(nicknameView: NicknameView) {
        this.nicknameView = nicknameView
    }

    //회원가입 API 호출
    fun getSignUp(signUpInfo: SignUpInfo){
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)

        logInService.getSignUp(signUpInfo).enqueue(object : Callback<SignUpResponse>{

            override fun onResponse(        //통신 성공했을 때
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                Log.d("signUp" , "통신 성공")
                signUpView.signUpSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {      //통신 실패 했을때
                Log.d("signUp" , "통신 실패")
                signUpView.signUpFailure("${t}")
            }
        })
    }


    //로그인 API
    fun getLogIn(logInInfo: LogInInfo) {
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getLogIn(logInInfo).enqueue(object : Callback<LogInResponse> {
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
                logInView.logInSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.d("LogIn" ,"통신 실패 : ${t}")
                logInView.logInFailure("${t}")
                Log.d("LogIn", "통신 실패 : ${t}")
            }
        })
    }

    // Email validation API
    fun getCheckEmail(emailInfo: EmailInfo) {
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getCheckEmail(emailInfo).enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                emailView.getEmailSuccess(response.body()!!)
                Log.d("checkEmailSuccess", "${response}")

            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                emailView.getEmailFailure("${t}")
                Log.d("checkEmailFailure", "${t}")
            }
        })
    }

    // Password validation API
    fun getCheckPassword(passwordInfo: PasswordInfo) {
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getCheckPassword(passwordInfo).enqueue(object : Callback<PasswordResponse> {
            override fun onResponse(
                call: Call<PasswordResponse>,
                response: Response<PasswordResponse>
            ) {
                passwordView.getPasswordSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PasswordResponse>, t: Throwable) {
                passwordView.getPasswordFailure("${t}")
            }
        })
    }

    // Nickname validation API
    fun getCheckNickname(nicknameInfo: NicknameInfo) {
        val logInService = getRetorfit().create(LoginRetrofitInterface::class.java)
        logInService.getCheckNickname(nicknameInfo).enqueue(object : Callback<NicknameResponse> {
            override fun onResponse(
                call: Call<NicknameResponse>,
                response: Response<NicknameResponse>
            ) {
                nicknameView.getNicknameSuccess(response.body()!!)

            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                nicknameView.getNicknameFailure("${t}")
            }
        })
    }
}