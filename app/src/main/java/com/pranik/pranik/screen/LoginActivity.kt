package com.pranik.pranik.screen

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.pranik.pranik.*
import com.pranik.pranik.databinding.ActivityLoginBinding
import com.pranik.pranik.modal.LoginPostModel
import com.pranik.pranik.user.UserDashboardActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

//    private var Login: Button? = null
//    lateinit var uName: AppCompatEditText
//    lateinit var uPassword: AppCompatEditText
    private var progressBar : ProgressBar?=null
    private var handler : Handler?=null
    private var progressIndicator : TextView?=null
     var UserName = "UserName"
    var Password = "Password"

    var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        title = "Pranik"

        binding.login.setOnClickListener {
            if (TextUtils.isEmpty(binding.userName.text.toString().trim())) {
                binding.userName.setError("User Name is Required !")
                binding.userName.requestFocus()
            }else if (TextUtils.isEmpty(binding.password.text.toString().trim())){
                binding.password.setError("Password is required !")
                binding.password.requestFocus()
            }
            else {
                checkLogin(binding.userName.text.toString().trim(),binding.password.text.toString().trim())
            }
    }
        binding.register.setOnClickListener {
                val Register = Intent(this, RegisterActivity::class.java)
                startActivity(Register)
            }
        }
    private fun checkLogin(username:String, password:String){
        binding.progressBarID.isVisible
        binding.login.isGone
        val retInstance = RetrofitInstance.getRetrofitInstance().
        create(ApiInterface::class.java)
        val signInInfo = LoginPostModel(username,password)
        retInstance.login(signInInfo)?.enqueue(object : retrofit2.Callback<UserClass?> {
            override fun onResponse(call: retrofit2.Call<UserClass?>, response: retrofit2.Response<UserClass?>
            )
            {
                if (!response.body()?.ReqStatus?.Status.toString().equals(false) &&
                    response.body()?.ReqStatus?.Message.toString() == "Success"
                )
                {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Successful !",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveAdminLoginData("${response.body()?.Result?.accessToken}",
                        "${response.body()?.Result?.userName}",
                        "${response.body()?.Result?.roleName}")
                }
                else{
//                    Toast.makeText(this@LoginActivity,"else block ! condition not satisfied !",Toast.LENGTH_SHORT).show()
                    binding.progressBarID.isGone
                    binding.login.visibility
                    Toast.makeText(
                        this@LoginActivity,
                        response.body()?.ReqStatus?.Message ,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<UserClass?>, t: Throwable) {
                binding.progressBarID.isGone
                binding.login.visibility
                Toast.makeText(
                    this@LoginActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun  saveAdminLoginData(accessToken:String,userName:String,userRole:String){
        val sharedPreference =  getSharedPreferences("PREFERENCE_PRANIK",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("username",userName)
        editor.putString("access_token", accessToken)
        editor.putString("userRole", userRole)
//        Log.e(TAG, "saveLoginData: LoginActivity->"+editor.putString("access_token", accessToken))
        editor.apply()
        Log.d(TAG, "saveAdminLoginData: we get the block !")
        when (userRole) {
            "Admin" -> {
                Log.d(TAG, "saveAdminLoginData: Admin block is here !")
                val login = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(login)
            }
            "USER" -> {
                Log.d(TAG, "saveAdminLoginData: User block is here !")
                val userLogin=Intent(this@LoginActivity, UserDashboardActivity::class.java)
                startActivity(userLogin)
            }
            else -> {
                Toast.makeText(this@LoginActivity,"Invalid User !",Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}
