package com.cittis.signsup.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.controller.firebase.tracking.TrackerService
import com.cittis.signsup.controller.plugins.TakePicture
import com.cittis.signsup.view.signal.Geolocation
import com.cittis.signsup.view.signal.PhotosGps
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // Main Variables
    var mainActivity: Activity = this
    var maxID: String = ""

    // Exception
    var exceptionMain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Init Process (All Actions in the tab MAIN)
        initProcess()
    }

    private fun initProcess() {
        // Init Permissions
        // Permissions
        Permissions(this).setPermissions()
        // Tracking
        startTracking()
    }

    override fun onSupportNavigateUp() =
        findNavController(nav_host_fragment).navigateUp()


    private fun startTracking() {

        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            //finish()
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ActionsRequest.PERMISSIONS_REQUEST
            )
        }
    }

    private fun startTrackerService() {
        startService(Intent(this, TrackerService::class.java))
        //finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == ActionsRequest.PERMISSIONS_REQUEST && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Start the service when the permission is granted
            startTrackerService()
        } else {
            //finish()
        }
    }

    // Camera Action // Return Intend Action
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Show Action And Values (Send Variables) - Intent
        Log.e("Main Activity", requestCode.toString() + "->" + resultCode + "->" + data.toString())

        // Get Fragment Called
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        // Select Case
        when (requestCode) {
            ActionsRequest.TAKE_PHOTO_REQUEST and RESULT_OK -> {
                // todo_ check gps
                val fragment = navHostFragment!!.childFragmentManager.fragments[0] as Geolocation
                // Call Method Fragment
                processCapturedPhoto(fragment.getTakePictureMain(""))
            }
            ActionsRequest.TAKE_PHOTO_GPS_REQUEST and RESULT_OK -> {
                // todo_ check gps
                val fragment = navHostFragment!!.childFragmentManager.fragments[0] as PhotosGps
                // Call Method Fragment
                processCapturedPhoto(fragment.getTakePictureMain(""))
            }

        }

    }

    private fun processCapturedPhoto(takePicture: TakePicture) {
        takePicture.processCapturedPhoto(takePicture.getPath())
    }

}
