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
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.cittis.signsup.R
import com.cittis.signsup.TrackingService
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
        //Check whether GPS tracking is enabled//

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish()
        }

        //Check whether this app has access to the location permission//

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        //If the location permission has been granted, then start the TrackerService//

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        } else {

            //If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        //If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            //...then start the GPS tracking service//

            startTrackerService()
        } else {

            //If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show()
        }
    }

//Start the TrackerService//

    private fun startTrackerService() {
        startService(Intent(this, TrackingService::class.java))

        //Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show()

        //Close MainActivity//

        //finish()
    }

    companion object {


        private val PERMISSIONS_REQUEST = 100
    }

}
