package com.cittis.signsup.controller.firebase.tracking

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.EditText
import com.cittis.signsup.R
import com.cittis.signsup.actions.EndPoints
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase


class TrackerService : Service() {

    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        buildNotification()
        requestLocationUpdates()
    }

    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        val builder = NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.tracking_enabled)
        startForeground(1, builder.build())
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val path =
            EndPoints.FireBasePath + "/" + EndPoints.FireBaseID //getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val ref = FirebaseDatabase.getInstance().getReference(path)
                    val location = locationResult!!.lastLocation
                    if (location != null) {
                        Log.d(TAG, "location update $location")
                        ref.setValue(location)
                    }
                }
            }, null)

        }
    }

    var dataTemp: Location? = null


    fun getLocation(
        context: Context,
        txt_latitude: EditText,
        txt_altitude: EditText,
        txt_longitude: EditText
    ) {
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(context)
        val path =
            EndPoints.FireBasePath + "/" + EndPoints.FireBaseID //getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            var tempData = client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val ref = FirebaseDatabase.getInstance().getReference(path)
                    val location: Location = locationResult!!.lastLocation
                    // Add Data
                    dataTemp = location

                    // Data
                    txt_latitude.setText(location.latitude.toString())
                    txt_altitude.setText(location.altitude.toString())
                    txt_longitude.setText(location.longitude.toString())

                    ref.setValue(location)
                }
            }, null)


        }
    }


    companion object {

        private val TAG = TrackerService::class.java.simpleName
    }

}
