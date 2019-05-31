package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.controller.firebase.tracking.TrackerService
import com.cittis.signsup.controller.plugins.TakePicture
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import kotlinx.android.synthetic.main.fragment_photos_gps.view.*

class PhotosGps : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()
    lateinit var takePicture: TakePicture

    // Get Location Main User
    private var locationMain: TrackerService = TrackerService()

    // Temp Variables

    var main = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_photos_gps, container, false)
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
            if (it.signal != null) {
                signalArrayList = it.signal!!
            }
        }
        if (login.firebase_auth == 1) {
            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {
        // Hidden Items
        hiddenItems()
        // Btn Camera
        cameraActions()
        // Btn GPS
        gpsActions()
        // Btn Save and Next
        btnSave()

    }

    private fun hiddenItems() {
        // Ids
        if (signalArrayList[signalArrayList.size - 1].typeSignal == "Horizontal") {
            viewMain.findViewById<LinearLayout>(R.id.linear_vertical_two).visibility = View.GONE
            viewMain.findViewById<LinearLayout>(R.id.linear_vertical_three).visibility = View.GONE
            //viewMain.findViewById<LinearLayout>(R.id.box_location).visibility = View.GONE
            //viewMain.findViewById<TextView>(R.id.lbl_location_signal).visibility = View.GONE
        } else {
            viewMain.findViewById<LinearLayout>(R.id.linear_vertical_two).visibility = View.VISIBLE
            viewMain.findViewById<LinearLayout>(R.id.linear_vertical_three).visibility = View.VISIBLE
            // viewMain.findViewById<LinearLayout>(R.id.box_location).visibility = View.VISIBLE
            // viewMain.findViewById<TextView>(R.id.lbl_location_signal).visibility = View.VISIBLE
        }
    }

    private fun cameraActions() {
        // Call on Click
        viewMain.findViewById<ImageButton>(R.id.ibtn_front).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_front, viewMain.cb_front, ActionsRequest.TAKE_PHOTO_GPS_REQUEST)

        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_back).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_back, viewMain.cb_back, ActionsRequest.TAKE_PHOTO_GPS_REQUEST)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_plaque).setOnClickListener {
            // Init Camera
            takePicture.setIButton(viewMain.ibtn_plaque, viewMain.cb_plaque, ActionsRequest.TAKE_PHOTO_GPS_REQUEST)
        }
    }

    private fun gpsActions() {
        // Set Actions
        viewMain.findViewById<ImageButton>(R.id.btn_gps).setOnClickListener {

            locationMain.getLocation(
                viewMain.context,
                viewMain.txt_latitude,
                viewMain.txt_altitude,
                viewMain.txt_longitude
            )

        }
    }


    private fun btnSave() {
        viewMain.findViewById<Button>(R.id.btn_next_photo).setOnClickListener {

            // Get Data Temp
            var tempData = getData()
            // 0 -> Latitud
            // 1 -> Longitud
            // 2 -> Altitud
            // 3 -> Img Front
            // 4 -> Img Back
            // 5 -> Img Plaque
            // 6 -> Location Trayecto


            // Set data temp - GPS PHOTO
            var cittusSignal = CittisSignal()
            cittusSignal.latitude = tempData[0].toFloat()
            cittusSignal.longitude = tempData[1].toFloat()
            cittusSignal.altitude = tempData[2].toFloat()
            cittusSignal.photoFront = tempData[3]
            if (signalArrayList[signalArrayList.size - 1].typeSignal == "Vertical") {
                cittusSignal.photoBack = tempData[4]
                cittusSignal.photoPlaque = tempData[5]
            }

            setData(cittusSignal)

        }
    }


    private fun setData(cittisSignal: CittisSignal) {
        // Make Object - Cittis Signup
        var signsUp: CittisSignsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.cittisSignal = cittisSignal
        // Reset
        signalArrayList[signalArrayList.size - 1] = signsUp
        // Add to DB
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)

        // Navigation
        Navigation.findNavController(viewMain).navigate(R.id.finish, bundle)

    }

    // Complements
    fun getData(): ArrayList<String> {
        // TODO: ISV DATA MAIN (1)
        // 0 -> Latitud
        // 1 -> Longitud
        // 2 -> Altitude
        // 3 -> Img Front
        // 4 -> Img Back
        // 5 -> Img Plaque
        // 6 -> Location Trayecto

        var tempArray: ArrayList<String> = ArrayList<String>()
        // Latitude and Longitude

        tempArray.add(0, viewMain.findViewById<EditText>(R.id.txt_latitude).text.toString()) // 0 -> Latitud
        tempArray.add(1, viewMain.findViewById<EditText>(R.id.txt_longitude).text.toString()) // 1 -> Longitud
        tempArray.add(2, viewMain.findViewById<EditText>(R.id.txt_altitude).text.toString()) // 1 -> Altitude


        // Get Imagen
        if (viewMain.findViewById<ImageButton>(R.id.ibtn_front).isClickable) {
            tempArray.add(3, getTakePictureMain("front").getPath()!!) // 3 -> Img Front
        } else {
            tempArray.add(3, "NONE")
        }

        if (signalArrayList[signalArrayList.size - 1].typeSignal == "Vertical") {

            if (viewMain.findViewById<ImageButton>(R.id.ibtn_back).isClickable) {//&& vertical){
                tempArray.add(4, getTakePictureMain("back").getPath()!!) // 4 -> Img Back
            } else {
                tempArray.add(4, "NONE")
            }

            if (viewMain.findViewById<ImageButton>(R.id.ibtn_plaque).isClickable) {//&& vertical){S
                tempArray.add(5, getTakePictureMain("plaque").getPath()!!) // 5 -> Img Plaque
            } else {
                tempArray.add(5, "NONE")
            }


        }
        return tempArray
    }


    fun getTakePictureMain(option: String): TakePicture {
        if (option != "") {
            when (option) {
                "front" -> takePicture.setIButton(
                    viewMain.ibtn_front,
                    viewMain.cb_front,
                    ActionsRequest.TAKE_PHOTO_GPS_REQUEST
                )
                "back" -> takePicture.setIButton(
                    viewMain.ibtn_back,
                    viewMain.cb_back,
                    ActionsRequest.TAKE_PHOTO_GPS_REQUEST
                )
                "plaque" -> takePicture.setIButton(
                    viewMain.ibtn_plaque,
                    viewMain.cb_plaque,
                    ActionsRequest.TAKE_PHOTO_GPS_REQUEST
                )
            }
        }

        return takePicture
    }


}
