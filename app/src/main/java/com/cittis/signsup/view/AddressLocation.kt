package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.cittis.signsup.R
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.LocationSignal

class AdressLocation : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser
    private var tempLocationSignal = ""


    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_address_location, container, false)
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
        saveAddress()
    }


    private fun getDataMain(): ArrayList<String> {
        // TODO: Get Data Main - General Data
        // 0 -> LocationBetween 1
        // 1 -> LocationBetween 2
        // 2 -> LocationBetween 3
        // 3 -> Starts 2 - State Signal
        var tempArray: ArrayList<String> = ArrayList<String>()

        var spinLocationBetween: Spinner = viewMain.findViewById<Spinner>(R.id.spin_location_between_signal)
        var txtLocationBetween: EditText = viewMain.findViewById<EditText>(R.id.txt_location_between_signal)

        var spinLocation: Spinner = viewMain.findViewById<Spinner>(R.id.spin_location_and_signal)
        var txtLocation: EditText = viewMain.findViewById<EditText>(R.id.txt_location_and_signal)

        var spinLocationSignal: Spinner = viewMain.findViewById<Spinner>(R.id.spin_location_with_signal)
        var txtLocationSignal: EditText = viewMain.findViewById<EditText>(R.id.txt_location_with_signal)


        tempLocationSignal = spinLocationBetween.selectedItem.toString() + " # " + txtLocationBetween.text.toString()
        tempArray.add(0, tempLocationSignal)

        tempLocationSignal = spinLocation.selectedItem.toString() + " # " + txtLocation.text.toString()
        tempArray.add(1, tempLocationSignal)

        tempLocationSignal = spinLocationSignal.selectedItem.toString() + " # " + txtLocationSignal.text.toString()
        tempArray.add(2, tempLocationSignal)

        return tempArray
    }

    private fun saveAddress() {

        viewMain.findViewById<Button>(R.id.btn_next_address).setOnClickListener {


            var locationSignal = LocationSignal()
            // Data temp
            var tempData = getDataMain()
            locationSignal.firstAddressSignal = tempData.get(0)
            locationSignal.secondAddressSignal = tempData.get(1)
            locationSignal.thirdAddressSignal = tempData.get(2)

            // Set Data Address
            setData(locationSignal)
        }
    }

    private fun setData(locationSignal: LocationSignal) {
        // Make Object - Cittis Signup
        var signsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.locationSignal = locationSignal
        // Reset
        signalArrayList[signalArrayList.size - 1] = signsUp
        // Add to DB
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)

    }
}
