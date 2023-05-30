package com.pranik.pranik.user.Fragment

import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pranik.pranik.R
import com.pranik.pranik.databinding.FragmentSecondBinding
import java.io.IOException
import java.util.*
import kotlin.math.*
import kotlin.Deprecated as Deprecated1


class SecondFragment : Fragment(), LocationListener {
    private lateinit var binding: FragmentSecondBinding
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
    private lateinit var inTime: AppCompatButton
    private lateinit var outTime:AppCompatButton
    private  lateinit var getLoc : AppCompatButton
    private var locationManager: LocationManager? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lat1 = 23.3771905
    private var lon1 = 85.3597775
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(activity,
//            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(activity,
//            android.R.string.no, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSecondBinding.inflate(inflater,container,false)
        getInTime()
        getOutTime()

        if (checkPermissions()){
            checkLocation()
            Log.e(TAG, "onCreateView: checkLocation"+checkLocation());
        }else{
            requestPermissions();
            Log.e(TAG, "onCreateView: requestPermissions");
        }
        
//        val fragment2 = SecondFragment()
//        val fragmentManager: FragmentManager? = fragmentManager
//        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
//        fragmentTransaction.replace(R.id.secondItem, fragment2)
//        fragmentTransaction.commit()
        return binding.root
    }

//    https://www.tutorialsbuzz.com/2019/09/android-timepicker-dialog-kotlin.html

    private fun getInTime(){
        inTime=binding.root.findViewById(R.id.inTime) as AppCompatButton
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                binding.currentTime1.text = String.format("%d : %d", hourOfDay, minute)
            }
        }, hour, minute, false)
        inTime.setOnClickListener {
            mTimePicker.show()
        }
    }
    private fun getOutTime() {
        outTime=binding.root.findViewById(R.id.outTime) as AppCompatButton
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                binding.currentTime2.text = String.format("%d : %d",hourOfDay,minute)
            }
        }, hour, minute, false)
        outTime.setOnClickListener {
            mTimePicker.show()
        }
    }
    private fun compareDistance(lat2: Double, lon2: Double) {
        val theta = lon1 - lon2
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2))* cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        punchAttendance(dist)
        Toast.makeText(context,"You are "+ String.format("%.2f",dist).toDouble()+" km away !",Toast.LENGTH_SHORT).show()
        Log.d(TAG, "compareDistance: Distance = "+String.format("%.2f",dist).toDouble()+" km")
    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
//        markAttendance(rad)
        return rad * 180.0 / Math.PI
    }
    private fun getLastLocation() {
        locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
//            Toast.makeText(context,"getLastLocation block is here !",Toast.LENGTH_SHORT).show()
            // here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//        Toast.makeText(context,"requestLocationUpdates"+locationManager.toString(),Toast.LENGTH_SHORT).show()
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 5f, this@SecondFragment as LocationListener)
        val criteria = Criteria()
        val bestProvider = locationManager!!.getBestProvider(criteria, true)
        val location: Location? = locationManager!!.getLastKnownLocation(bestProvider!!)
        activity?.let {
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    Log.e(TAG, "getLastLocation: task - > "+task.result.provider.toString())
//                    getLoc = task.result
                    //                latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
                    //                longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
                    Log.e(TAG, "getLastLocation: latitude&longitude -> "+task.result.toString())
                    Log.d(TAG, "getLastLocation: "+location.toString())
                    if (location != null) {
                        onLocationChanged(location)
//                        Toast.makeText(context,"Test 2",Toast.LENGTH_SHORT).show()
                    }
//                    Toast.makeText(context, "getLastLocation : latitude&longitude -> " + task.result.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showMessage("No location detected. Make sure location is enabled on the device.")
                }
            }
        }
    }
    override fun onLocationChanged(location: Location) {
        val addresses: List<Address>
        val geocoder = activity?.let { Geocoder(it, Locale.getDefault()) }
//        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val latitude = location.latitude
        val longitude = location.longitude
        Log.e("latitude", "longitude--$longitude")
        try {
            compareDistance(latitude,longitude)
            Log.e("latitude", "inside latitude--$latitude")
            addresses = geocoder?.getFromLocation(latitude, longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address: String = addresses[0].getAddressLine(0)
                val city: String = addresses[0].locality
                val state: String = addresses[0].adminArea
                val country: String = addresses[0].countryName
                val postalCode: String = addresses[0].postalCode
                val knownName: String = addresses[0].featureName
//                Log.d(TAG, "onLocationChanged: "+address+"\n"+city+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+knownName)
                Toast.makeText(context,"Details : "+address+"\n"+city+"\n"+state+"\n"+country+"\n"+postalCode, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context,"Location : "+latitude.toString()+"\n"+longitude.toString(), Toast.LENGTH_SHORT).show()
//                markAttendance(rad2deg(rad = Double.MAX_VALUE))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "onLocationChanged: Error in onLocationChanged block "+e.message.toString())
        }
    }
    private fun punchAttendance(distance: Double):Double {
        if(distance <= 3)
        {
//            Toast.makeText(context,"Distance 1 : "+String.format("%.2f",distance).toDouble(),Toast.LENGTH_SHORT).show()
            attendanceDialogTrue()
        }
        else if(distance > 3)
        {
//            Toast.makeText(context,"Distance 2 : "+String.format("%.2f",distance).toDouble(),Toast.LENGTH_SHORT).show()
            attendanceDialogFalse()
        }
        return distance
    }
    private fun attendanceDialogTrue(){
        val alertDia= AlertDialog.Builder(context)
        with(alertDia)
        {
            setTitle("Attendance status")
            setMessage("Attendance punched successfully !")
            setPositiveButton("OK",DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            show()
        }
    }
    private fun attendanceDialogFalse(){
        val alertDia= AlertDialog.Builder(context)
        with(alertDia)
        {
            setTitle("Attendance status")
            setMessage("Sorry ! Attendance can't be punched !\nMinimum range should be 3 kms \n" +
                    " In order to punch attendance !")
            setPositiveButton("OK",DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            show()
        }
    }

    private fun showMessage(string: String) {
        val container = binding.root.findViewById<View>(R.id.frameLayoutAttendance)
        if (container != null) {
            Toast.makeText(context, string, Toast.LENGTH_LONG).show()
        }
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(context, mainTextStringId.toString()+"\n"+actionStringId.toString(), Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
        startLocationPermissionRequest()
    }
    private fun startLocationPermissionRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }
    private fun requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withActivity(activity)
            // below line is use to request the number of permissions which are required in our app.
            .withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    // this method is called when all permissions are granted
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        // do you work now
                        Toast.makeText(context, "All the permissions are granted..", Toast.LENGTH_SHORT).show()
                    }
                    // check for permanent denial of any permission
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently, we will show user a dialog message.
//                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    // this method is called when user grants some permission and denies some of them.
                    permissionToken?.continuePermissionRequest()
                }
            }).withErrorListener {
                // we are displaying a toast message for error message.
                Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            // below line is use to run the permissions on same thread and to check the permissions
            .onSameThread().check()
    }
    @Deprecated1("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied",   "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
    private fun checkLocation() {
        getLoc=binding.root.findViewById(R.id.locationBtn) as AppCompatButton
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        getLoc.setOnClickListener {
            getLastLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token","")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole","")
        checkLocation()
    }
}