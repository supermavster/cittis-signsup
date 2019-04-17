package com.cittis.signsup.controller

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import com.cittis.signsup.actions.ActionsRequest

class Permissions(private var context: FragmentActivity, private var onlyCam: Boolean = false) {

    // Permissions
    private var arrayPermissions = arrayOf(READ_EXTERNAL_STORAGE, CAMERA, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    private val list: ArrayList<String> = ArrayList()

    init {

        for (permission in arrayPermissions) {
            list.add(permission)
        }
    }

    fun setPermissions() {
        if (!checkPermissions()) requestPermission()
    }

    private fun checkPermissions(): Boolean {
        for (permission in list) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            context, arrayPermissions,
            ActionsRequest.PERMISSION_REQUEST_CODE
        )
    }
}