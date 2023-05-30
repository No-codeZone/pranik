package com.pranik.pranik.user.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pranik.pranik.R
import com.pranik.pranik.databinding.FragmentSettingBinding
import com.pranik.pranik.databinding.FragmentThirdBinding
import com.pranik.pranik.screen.LoginActivity

class ThirdFragment : Fragment() {
    private lateinit var binding: FragmentThirdBinding
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_third, container, false)
        binding= FragmentThirdBinding.inflate(inflater,container,false)
        binding.logout.setOnClickListener {
            val editor=sharedPreference?.edit()
            editor?.clear()
            editor?.apply()
            Toast.makeText(context,"Logout Successful !", Toast.LENGTH_SHORT).show()
            activity?.let {
                val logout= Intent(context, LoginActivity::class.java)
                startActivity(logout)
            }
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token", "")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole","")
//        Toast.makeText(context,sharedPreference?.getString("userRole",""),Toast.LENGTH_SHORT).show()
//        getProfile("bearer "+sharedPreference?.getString("access_token", "")!!)
    }
}