package com.pranik.pranik.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranik.pranik.ApiInterface
import com.pranik.pranik.R
import com.pranik.pranik.RetrofitInstance
import com.pranik.pranik.adapters.AdapterRcView
import com.pranik.pranik.adapters.AdapterRcView.OnItemClickListener
import com.pranik.pranik.databinding.FragmentHomeBinding
import com.pranik.pranik.modal.GetGeoLocationModal
import com.pranik.pranik.modal.RequestBodySaveUpdateCoordinates
import com.pranik.pranik.modal.ResponseSaveUpdateCoordinate
import com.pranik.pranik.modal.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  HomeFragment : Fragment() {
//    var list= ArrayList<GetGeoLocationModal>()
    private lateinit var binding : FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterRcView: AdapterRcView
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater,container, false)
        recyclerView = binding.root.findViewById(R.id.rcViewUserList) as RecyclerView
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token","")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole","")
//        Toast.makeText(context,"HomeFragment is here !",Toast.LENGTH_SHORT).show()
//        Toast.makeText(context,sharedPreference?.getString("userRole",""), Toast.LENGTH_SHORT).show()
        functiongetGeoLocation("bearer "+sharedPreference?.getString("access_token", "")!!)
        return binding.root
    }
    private fun functiongetGeoLocation(token : String){
        print("Token = $token")
        val retInstance=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        retInstance.getGeoLocation(token).enqueue(object : Callback<GetGeoLocationModal?>{
            override fun onResponse(
                call: Call<GetGeoLocationModal?>,
                response: Response<GetGeoLocationModal?>
            ) {
                print("Pagination elements(ID)s "+response.code())
                if (response.code()==200){
                    try {
                        adapterRcView = AdapterRcView(response.body()!!.Result, context = HomeFragment(),
                            listener = object :
                            OnItemClickListener {
                            override fun alertDialog(position: Int, list: ArrayList<Result>) {
                                showAlertDialog(list[position])
//                                Toast.makeText(context,"ID : "+position,Toast.LENGTH_LONG).show()
//                                Toast.makeText(context,""+list[position],Toast.LENGTH_SHORT).show()
                             }
                        })
//
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        recyclerView.adapter = adapterRcView
                    }catch (e:java.lang.Exception){
                        Log.e(TAG, "onResponse: Error"+e.message.toString() )
                    }
                }
            }
            override fun onFailure(call: Call<GetGeoLocationModal?>, t: Throwable) {
                Log.e(TAG, "onFailure: "+t.message)
                call.cancel()
            }
        })
    }

    fun  showAlertDialog(result: Result) {
        val builder = AlertDialog.Builder(activity).create()
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_home_fragment,null)
        val id = view?.findViewById<AppCompatEditText>(R.id.dBoxID)
        id?.setText(result.Id.toString())
        val lat=view?.findViewById<AppCompatEditText>(R.id.dBoxLat)
        lat?.setText(result.Latitude.toString())
        val lon=view?.findViewById<AppCompatEditText>(R.id.dBoxLon)
        lon?.setText(result.Longitude.toString())
        val range=view?.findViewById<AppCompatEditText>(R.id.dBoxRange)
        range?.setText(result.Range.toString())
        val status=view?.findViewById<AppCompatEditText>(R.id.dBoxStatus)
        status?.setText(result.IsActive.toString())
        val title=view?.findViewById<AppCompatEditText>(R.id.dBoxTitle)
        title?.setText(result.Title.toString())

        builder.listView
        val  button = view?.findViewById<Button>(R.id.editSave)
        val button1=view?.findViewById<Button>(R.id.editCancel)
        builder.setView(view)
        button?.setOnClickListener {
            sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
            sharedPreference?.getString("access_token", "")
            Toast.makeText(context,"Clicked !",Toast.LENGTH_SHORT).show()
            Log.d(TAG, "showAlertDialog: result->" +result.toString())
            val retrofitInstance=RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
            val reqSaveCord=RequestBodySaveUpdateCoordinates()
            retrofitInstance.dialogMsg("bearer "+sharedPreference?.getString("access_token",""),
                reqSaveCord).enqueue(object : Callback<ResponseSaveUpdateCoordinate>{
                override fun onResponse(
                    call: Call<ResponseSaveUpdateCoordinate>,
                    response: Response<ResponseSaveUpdateCoordinate>
                ) {
                    //We r getting proper response here
                    Toast.makeText(context, "onResponse block $result",Toast.LENGTH_SHORT).show()
                    //But here we r getting response code as Bad request(400)
                    Toast.makeText(context,"onResponse block "+response.code().toString(),Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<ResponseSaveUpdateCoordinate>, t: Throwable) {
                    Toast.makeText(context,"onResponse block "+t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })
            builder.dismiss()
        }
        button1?.setOnClickListener {
//            Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
    override fun onResume() {
        super.onResume()
        val secs = 1 * 1550
        val dlg = ProgressDialog(context)
        dlg.setMessage("Processing...")
        dlg.setCancelable(false)
        dlg.show()
        Handler().postDelayed({ dlg.dismiss() }, secs.toLong())
    }
//    retrofitInstance.dialogMsg("bearer " +sharedPreference?.getString("access_token", ""),
//    RequestBodySaveUpdateCoordinates()
//    ).enqueue(object : Callback<ResponseSaveUpdateCoordinate>{
//        override fun onResponse(
//            call: Call<ResponseSaveUpdateCoordinate>,
//            response: Response<ResponseSaveUpdateCoordinate>
//        )
//        {
//            Toast.makeText(context,"Response block is here ! "+response.message().toString(),
//                Toast.LENGTH_SHORT).show()
//            Log.e(TAG, "onResponse: Response block "+response.message().toString())
//            try {
//                if (response.code()==200){
//                    Log.d(TAG, "onResponse: Response -> "+response.isSuccessful.toString())
//                    val idKey=view.findViewById<AppCompatEditText>(R.id.dBoxID)
//                    idKey.setText(result.Id.toString())
//                    val latKey=view.findViewById<AppCompatEditText>(R.id.dBoxLat)
//                    latKey.setText(result.Latitude.toString())
//                    val lonKey=view.findViewById<AppCompatEditText>(R.id.dBoxLon)
//                    lonKey.setText(result.Longitude.toString())
//                    val ranKey=view.findViewById<AppCompatEditText>(R.id.dBoxRange)
//                    ranKey.setText(result.Range.toString())
//                    val statusKey=view.findViewById<AppCompatEditText>(R.id.dBoxStatus)
//                    statusKey.setText(result.IsActive.toString())
//                    val titleKey=view.findViewById<AppCompatEditText>(R.id.dBoxTitle)
//                    titleKey.setText(result.Title.toString())
//                    retrofitInstance.dialogMsg("bearer "+sharedPreference?.
//                    getString("access_token", ""),
//                        RequestBodySaveUpdateCoordinates()
//                    ).enqueue(object :Callback<ResponseSaveUpdateCoordinate>{
//                        override fun onResponse(
//                            call: Call<ResponseSaveUpdateCoordinate>,
//                            response: Response<ResponseSaveUpdateCoordinate>
//                        ) {
//                            Log.d(TAG, "onResponse: SaveUpdated Block"+response.body().toString())
//                        }
//                        override fun onFailure(
//                            call: Call<ResponseSaveUpdateCoordinate>,
//                            t: Throwable
//                        ) {
//                            Log.e(TAG, "onFailure: saveNotUpdated Block"+t.message.toString())
//                        }
//                    })
////                            Toast.makeText(context,"Response ID-> " +idKey.setText(result.Id.toString()),Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show()
//                }
//            }catch (e:Exception){
//                Log.e(TAG, "onResponse: Exception -> "+e.message.toString())
//            }
//        }
//        override fun onFailure(call: Call<ResponseSaveUpdateCoordinate>, t: Throwable) {
////                    Toast.makeText(context,"Failure block is here !"+t.message.toString(), Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "onFailure: -> "+t.message.toString())
//        }
//    })
}