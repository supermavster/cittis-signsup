package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.controller.plugins.TakePicture
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.GeolocationCardinalImages
import kotlinx.android.synthetic.main.fragment_geolocation.view.*

class Geolocation : Fragment() {

    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    lateinit var takePicture: TakePicture


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_geolocation, container, false)

        takePicture = TakePicture(this.activity!!)

        return viewMain

    }

    // TODO: Get Data - Login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get data from last Fragment
        val someDataClass: CittisListSignal? = arguments?.getParcelable("CittisDB")
        cittisDB = someDataClass!!

        someDataClass.let {
            login = it.dataUser!!
        }
        if (login.firebase_auth == 1) {

            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {
        // Btn Camera
        cameraGeolocalization()
        // Btn Save and Next
        btnSave()
    }

    private fun cameraGeolocalization() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_north).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_north, viewMain.cb_north, ActionsRequest.TAKE_PHOTO_REQUEST)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_south).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_south, viewMain.cb_south, ActionsRequest.TAKE_PHOTO_REQUEST)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_west).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_west, viewMain.cb_west, ActionsRequest.TAKE_PHOTO_REQUEST)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_east).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_east, viewMain.cb_east, ActionsRequest.TAKE_PHOTO_REQUEST)
        }
    }

    private fun btnSave() {
        viewMain.findViewById<Button>(R.id.btn_save_geolocalization).setOnClickListener {
            var geolocationCardinalImages = ArrayList<GeolocationCardinalImages>()

            var geoImagen = GeolocationCardinalImages("North", getTakePictureMain("North").getPath()!!)
            geolocationCardinalImages.add(geoImagen)

            geoImagen = GeolocationCardinalImages("South", getTakePictureMain("South").getPath()!!)
            geolocationCardinalImages.add(geoImagen)

            geoImagen = GeolocationCardinalImages("East", getTakePictureMain("West").getPath()!!)
            geolocationCardinalImages.add(geoImagen)

            geoImagen = GeolocationCardinalImages("West", getTakePictureMain("East").getPath()!!)
            geolocationCardinalImages.add(geoImagen)


            // Make Object    ArrayList<CittusISV>()
            cittisDB.geolocationCardinalImages = geolocationCardinalImages
            // Show Data
            Log.e("Data-Login", cittisDB.toString())
            // Set and Send Data Main
            bundle.putParcelable("CittisDB", cittisDB)
            // Init Action
            Navigation.findNavController(viewMain).navigate(R.id.typeSignal, bundle)

        }
    }

    fun getTakePictureMain(option: String): TakePicture {
        if (option != "") {
            when (option) {
                "North" -> takePicture.setIButton(viewMain.ibtn_north, viewMain.cb_north)
                "South" -> takePicture.setIButton(viewMain.ibtn_south, viewMain.cb_south)
                "West" -> takePicture.setIButton(viewMain.ibtn_west, viewMain.cb_west)
                "East" -> takePicture.setIButton(viewMain.ibtn_east, viewMain.cb_east)
            }
        }

        return takePicture
    }

}
