package com.pranik.pranik.fragment
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pranik.pranik.screen.LoginActivity
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import coil.load
import com.pranik.pranik.*
import com.pranik.pranik.databinding.FragmentSettingBinding
import com.pranik.pranik.modal.RequestSaveProfile
import com.pranik.pranik.modal.ResponseSaveProfile
import com.pranik.pranik.profile.ProfileModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
//    private lateinit var viewOfLayout: View
    private lateinit var binding : FragmentSettingBinding
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
//    val userName=view?.findViewById<AppCompatEditText>(R.id.dBoxName)
//    val userMail=view?.findViewById<AppCompatEditText>(R.id.dBoxMail)
//    val userContact=view?.findViewById<AppCompatEditText>(R.id.dBoxContact)

    //    lateinit var userName : TextView
    var user_nameKey = "userName"
    var username = ""
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingBinding.inflate(inflater,container,false)
        binding.logout.setOnClickListener {
            val editor=sharedPreference?.edit()
            editor?.clear()
            editor?.apply()
            Toast.makeText(context,"Logout Successful !", Toast.LENGTH_SHORT).show()
            activity?.let {
                val logout=Intent(context, LoginActivity::class.java)
                startActivity(logout)
            }
        }
        return binding.root
    }

    private fun displayAlertDialog(
        user_nameTest: String,
        user_mailTest: String,
        user_numberTest: String
    ) {
        val builder = activity?.let { AlertDialog.Builder(it).create() }
        val view=activity?.layoutInflater?.inflate(R.layout.dialog_profile_fragment, null)
        builder?.listView
        val retrofitInstance=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        retrofitInstance.updateProfile("bearer "+sharedPreference?.getString("access_token", ""),
            RequestSaveProfile()).enqueue(object : Callback<ResponseSaveProfile>{
            override fun onResponse(
                call: Call<ResponseSaveProfile>,
                response: Response<ResponseSaveProfile>
            ) {
                Log.d(TAG, "onResponse: savedProfile-> "+response.body().toString())
                try{
                    val retroInstance=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
                    retroInstance.getProfile("bearer "+sharedPreference?.getString("access_token","")).enqueue(object : Callback<ProfileModel>{
                        override fun onResponse(
                            call: Call<ProfileModel>,
                            response: Response<ProfileModel>
                        ) {
                            Toast.makeText(context,"onResponse block is here !",Toast.LENGTH_SHORT).show()
                            val userName=view?.findViewById<AppCompatEditText>(R.id.dBoxName)
                            val userMail=view?.findViewById<AppCompatEditText>(R.id.dBoxMail)
                            val userContact=view?.findViewById<AppCompatEditText>(R.id.dBoxContact)
                            val button= view?.findViewById<Button>(R.id.editSaveProfile)
                            val button1= view?.findViewById<Button>(R.id.editCancelProfile)
                            userName?.setText(user_nameTest)
                            userMail?.setText(user_mailTest)
                            userContact?.setText(user_numberTest)
                            builder?.setView(view)
                            button?.setOnClickListener {
                                val retrofit=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
                                retrofit.updateProfile("bearer "+sharedPreference?.getString("access_token",""),
                                    RequestSaveProfile()).enqueue(object : Callback<ResponseSaveProfile>{
                                    override fun onResponse(
                                        call: Call<ResponseSaveProfile>,
                                        response: Response<ResponseSaveProfile>
                                    ) {
                                        Toast.makeText(context,"Profile updated successfully !",Toast.LENGTH_SHORT).show()
                                    }
                                    override fun onFailure(
                                        call: Call<ResponseSaveProfile>,
                                        t: Throwable
                                    ) {
                                     Toast.makeText(context,""+t.message.toString(),Toast.LENGTH_SHORT).show()
                                    }
                                })
                                builder?.dismiss()
                            }
                            button1?.setOnClickListener {
                                builder?.dismiss()
                            }
                            builder?.setCanceledOnTouchOutside(false)
                            builder?.show()
                            Toast.makeText(context,"Response"+response.message().toString(),Toast.LENGTH_SHORT).show()
                        }
                        override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                            Toast.makeText(context,"Oops ! Something went wrong ! !",Toast.LENGTH_SHORT).show()
                        }
                    })
                }catch (e:Exception){
                    Log.e(TAG, "onResponse: Error profileFrag(DialogBox)-> "+e.message.toString())
                }
            }
            override fun onFailure(call: Call<ResponseSaveProfile>, t: Throwable) {
                Log.e(TAG, "onFailure: profileNotSaved->"+t.message.toString())
            }
        })
    }

    fun getProfile(token : String){
        val retInstance = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        retInstance.getProfile(token).enqueue(object : retrofit2.Callback<ProfileModel?> {

            override fun onFailure(call: retrofit2.Call<ProfileModel?>, t: Throwable) {
                call.cancel()
            }
            override fun onResponse(call: Call<ProfileModel?>, response: Response<ProfileModel?>) {
                binding.pranikGrp.setText(response.body()?.Result?.FullName)
                val user_name=binding.usernameProfile.setText(response.body()?.Result?.UserName.toString())
                val user_mail=binding.useremailProfile.setText(response.body()?.Result?.Email.toString())
                val user_number=binding.usernumberProfile.setText(response.body()?.Result?.PhoneNumber.toString())
                lifecycleScope.launch {
//                    val retroImg=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
//                    retroImg.getProfile("bearer"+sharedPreference?.getString("access_token","")).enqueue(object : Callback<ProfileModel>{
//                        override fun onResponse(
//                            call: Call<ProfileModel>,
//                            response: Response<ProfileModel>
//                        ) {
                            val imageUrl=response.body()?.Result?.ImageUrl
                            binding.imgUpload.load(imageUrl) {
//                                crossfade(true)
//                                placeholder(R.drawable.placeholder_image)
                                error(R.drawable.error_image)
//                                size(300, 300)
                            }
                        }
//                        override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
//                            Log.d(TAG, "onFailure: Profile(Image)-> "+t.message.toString())
//                        }
//                    })
//                }
                try {
                    if(response.code()==200){
                        binding.editProfile.setOnClickListener {
                            displayAlertDialog(response.body()?.Result?.UserName.toString(),
                                response.body()?.Result?.Email.toString(),response.body()?.Result?.PhoneNumber.toString())
//                            Toast.makeText(context,"Response block is here !",Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (exception:Exception){
                    Log.e(TAG, "onResponse: Exception(Profile) -> "+exception.message.toString())
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token", "")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole","")
//        Toast.makeText(context,sharedPreference?.getString("userRole",""),Toast.LENGTH_SHORT).show()
        getProfile("bearer "+sharedPreference?.getString("access_token", "")!!)
    }
}