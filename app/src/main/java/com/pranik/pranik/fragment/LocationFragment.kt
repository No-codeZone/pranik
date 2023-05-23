package com.pranik.pranik.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pranik.pranik.R
import com.pranik.pranik.databinding.FragmentMenuBinding
import com.pranik.pranik.databinding.FragmentSecondBinding
import com.pranik.pranik.user.Fragment.SecondFragment


class LocationFragment : Fragment() {
//    private var locationManager: LocationManager? = null
    private lateinit var binding: FragmentMenuBinding
//    private lateinit var viewOfLayout: View
//    private val locationPermissionCode = 2
    var sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var displayLoc : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMenuBinding.inflate(inflater,container,false)
        sharedPreference = activity?.getSharedPreferences("PREFERENCE_PRANIK", Context.MODE_PRIVATE)
        sharedPreference?.getString("access_token","")
        sharedPreference?.getString("username", "")
        sharedPreference?.getString("userRole","")
        if (checkPermissions()){
            checkLocation()
            Log.e(TAG, "onCreateView: checkLocation"+checkLocation());
        }else{
            requestPermissions();
            Log.e(TAG, "onCreateView: requestPermissions");
        }
        return binding.root
    }

    private fun checkLocation() {
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        getLastLocation()
    }

    private fun getLastLocation() {
//        Toast.makeText(context,"Clicked !",Toast.LENGTH_SHORT).show()
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
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        activity?.let {
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    displayLoc=binding.root.findViewById(R.id.getLoc) as TextView
                    displayLoc.text="Latitude : "+task.result.latitude.toString()+"\nLongitude : "+task.result.longitude.toString()
//                    getLoc = task.result
                    //                latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
                    //                longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
                    Log.e(LocationFragment.TAG, "getLastLocation: latitude&longitude -> "+task.result.toString())
//                    Toast.makeText(context, "getLastLocation : latitude&longitude -> " + task.result.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(LocationFragment.TAG, "getLastLocation:exception", task.exception)
                    showMessage("No location detected. Make sure location is enabled on the device.")
                }
            }
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
        Toast.makeText(context, mainTextStringId, Toast.LENGTH_LONG).show()
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
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                SecondFragment.REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
    private fun requestPermissions() {
//        val shouldProvideRationale = activity?.let {
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                it,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        }
//        if (shouldProvideRationale == true) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//            showSnackbar("Location permission is needed for core functionality", "Okay",
//                View.OnClickListener {
//                    startLocationPermissionRequest()
//                })
//        }
//        else {
//            Log.i(TAG, "Requesting permission")
//            startLocationPermissionRequest()
//        }
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withActivity(activity)
            // below line is use to request the number of permissions which are required in our app.
            .withPermissions(Manifest.permission.CAMERA,
                // below is the list of permissions
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS)
            // after adding permissions we are calling an with listener method.
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
    @Deprecated("Deprecated in Java")
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
                    showSnackbar("Permission was denied", "Settings",
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
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}