package com.pranik.pranik.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pranik.pranik.R
import com.pranik.pranik.databinding.ActivityDashboardBinding
import com.pranik.pranik.fragment.HomeFragment
import com.pranik.pranik.fragment.LocationFragment
import com.pranik.pranik.fragment.ProfileFragment
import com.pranik.pranik.user.Fragment.SecondFragment

//import com.pranik.pranik.fragment.settingFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        loadFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.location -> {
                    loadFragment(LocationFragment())
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, fragment)
        transaction.commit()
    }
}