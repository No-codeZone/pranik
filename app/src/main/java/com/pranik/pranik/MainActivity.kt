package com.pranik.pranik

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import com.pranik.pranik.databinding.ActivityMainBinding
import com.pranik.pranik.screen.DashboardActivity
import com.pranik.pranik.screen.LoginActivity
import com.pranik.pranik.user.UserDashboardActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        
    }
    fun getSessionData() {
        val sharedPreference = getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
//        val sharedPreferenceUser = getSharedPreferences("PREFERENCE_PRANIK1", Context.MODE_PRIVATE)
        sharedPreference.getString("access_token","")
        sharedPreference.getString("username", "")
        sharedPreference.getString("userRole","")
        Log.e(TAG, "getSessionData: "+sharedPreference.getString("access_token",""))
        Log.e(TAG, "getSessionData: "+sharedPreference.getString("userRole",""))
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreference.getString("access_token","")?.isEmpty()==true){
                val intent = Intent(this, LoginActivity::class.java)
//                Toast.makeText(this,"Invalid credentials ! Try again",Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }else{
//                Log.e(TAG, "getSessionData: "+sharedPreference.getString("access_token",""))
//                Log.e(TAG, "getSessionData: "+sharedPreference.getString("userRole",""))
                if (sharedPreference.getString("userRole","").equals("Admin")){
//                    Log.e(TAG, "getSessionData: "+sharedPreference.getString("access_token",""))
                    val intent = Intent(this, DashboardActivity::class.java)
//                    Toast.makeText(this,"User->"+sharedPreference.getString("userRole",""),Toast.LENGTH_SHORT).show()
                    Log.d(TAG,"Admin Dashboard is here !")
                    startActivity(intent)
                    finish()
                }else if (sharedPreference.getString("userRole","").equals("USER")){
                    Log.e(TAG, "getSessionData: "+sharedPreference.getString("access_token","") +sharedPreference.getString("userRole",""))
                    val intent = Intent(this, UserDashboardActivity::class.java)
//                    Toast.makeText(this,"User Dashboard is here !",Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "getSessionData: User -> "+sharedPreference.getString("userRole","").toString())
                    startActivity(intent)
                    finish()
                }else if(sharedPreference.getString("userRole","")?.isEmpty()==true
                    && sharedPreference.getString("access_token","")?.isEmpty()==true){
                //                    Log.e(TAG, "getSessionData:Error ", )
                    Toast.makeText(this,"Invalid credentials ! Try again",Toast.LENGTH_SHORT).show()
                }
            }
        },3500)
    }
    override fun onResume() {
        super.onResume()
        getSessionData();
    }
}