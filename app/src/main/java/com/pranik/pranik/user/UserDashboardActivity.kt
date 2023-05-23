package com.pranik.pranik.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pranik.pranik.R
import com.pranik.pranik.databinding.ActivityUserDashboardBinding
import com.pranik.pranik.user.Fragment.FirstFragment
import com.pranik.pranik.user.Fragment.SecondFragment
import com.pranik.pranik.user.Fragment.ThirdFragment

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_user_dashboard)
        loadFragment(FirstFragment())
        binding.userBottomNavigationView.setOnItemReselectedListener { 
            when(it.itemId){
              R.id.firstItem->{
                  loadFragment(FirstFragment())
                  true
                }
                R.id.secondItem->{
                    loadFragment(SecondFragment())
                    true
                }
                R.id.thirdItem->{
                    loadFragment(ThirdFragment())
                    true
                }
        }
        }
    }
    private fun loadFragment(fragment:Fragment){
        val transactionUser=supportFragmentManager.beginTransaction()
        transactionUser.replace(R.id.userFragment, fragment)
        transactionUser.commit()
    }
}