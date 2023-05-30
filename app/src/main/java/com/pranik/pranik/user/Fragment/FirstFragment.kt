package com.pranik.pranik.user.Fragment

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pranik.pranik.ApiInterface
import com.pranik.pranik.RetrofitInstance
import com.pranik.pranik.databinding.FragmentFirstBinding
import com.pranik.pranik.profile.UserProfileModel
import retrofit2.Call
import retrofit2.Response


class FirstFragment : Fragment() {
    private lateinit var binding: FragmentFirstBinding
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getUser(token: String) {
        val retInstance = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        retInstance.getUserProfile(token).enqueue(object : retrofit2.Callback<UserProfileModel?> {

            override fun onFailure(call: retrofit2.Call<UserProfileModel?>, t: Throwable) {
                call.cancel()
            }

            override fun onResponse(
                call: Call<UserProfileModel?>,
                response: Response<UserProfileModel?>
            ) {
                try {
                    if (response.code() == 200) {
//                        Toast.makeText(
//                            context,
//                            "Response--> " + response.body().toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
                        val uName=response.body()?.Result?.FullName.toString()
                        val uEmail=response.body()?.Result?.Email.toString()
                        binding.userDashbrdName.text=uName
                        binding.userDashbrdLoc.text=uEmail
                        Log.d(
                            TAG,
                            "onResponse: UserProfile(First Fragment) " + response.body().toString()
                        )
                    }
                } catch (exception: Exception) {
                    Log.e(
                        TAG,
                        "onResponse: Exception(UserProfile) -> " + exception.message.toString()
                    )
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val secs = 1 * 1550
        val dlg = ProgressDialog(context)
        dlg.setMessage("Processing...")
        dlg.setCancelable(false)
        dlg.show()
        Handler().postDelayed({ dlg.dismiss() }, secs.toLong())
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token", "")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole", "")
//        Toast.makeText(context, sharedPreference?.getString("userRole", ""), Toast.LENGTH_SHORT)
//            .show()
        getUser("bearer " + sharedPreference?.getString("access_token", "")!!)
    }
}