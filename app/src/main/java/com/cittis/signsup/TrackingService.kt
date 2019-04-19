package com.cittis.signsup

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TrackingService : Service() {

    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            //Unregister the BroadcastReceiver when the notification is tapped//

            unregisterReceiver(this)

            //Stop the Service//

            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        buildNotification()
        loginToFirebase()
    }

    //Create the persistent notification//

    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create the persistent notification//
        val builder = Notification.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.tracking_enabled_notif))

            //Make this notification ongoing so it can’t be dismissed by the user//

            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.tracking_enabled)
        startForeground(1, builder.build())
    }

    private fun loginToFirebase() {

        //Authenticate with Firebase, using the email and password we created earlier//

        val email = getString(R.string.test_email)
        val password = getString(R.string.test_password)

        //Call OnCompleteListener if the user is signed in successfully//

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            //If the user has been authenticated...//

            if (task.isSuccessful) {

                //...then call requestLocationUpdates//

                requestLocationUpdates()
            } else {

                //If sign in fails, then log the error//

                Log.d(TAG, "Firebase authentication failed")
            }
        }
    }

    //Initiate the request to track the device's location//

    private fun requestLocationUpdates() {
        val request = LocationRequest()

        //Specify how often your app should request the device’s location//

        request.interval = 10000

        //Get the most accurate location data available//

        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val path = getString(R.string.firebase_path)
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        //If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

            //...then request location updates//

            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {

                    //Get a reference to the database, so your app can perform read and write operations//

                    val ref = FirebaseDatabase.getInstance().getReference(path)
                    val location = locationResult!!.lastLocation
                    if (location != null) {

                        //Save the location data to the database//

                        ref.setValue(location)
                    }
                }
            }, null)
        }
    }

    companion object {

        private val TAG = TrackingService::class.java.simpleName
    }
}