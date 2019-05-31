package com.cittis.signsup.view.signal.vertical

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.model.*

class VerticalInformativeSignal : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()
    // Variables Class
    var verticalNameSignal = ""
    var idSiganl = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_vertical_informative_signal, container, false)
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
        // BTN Services
        btnServices()
        // BTN Touristic
        btnTouristic()
        // BTN Location
        btnLocation()
    }

    private fun btnServices() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vts_informative_services).setOnClickListener { view ->
            makeActivityImages(R.string.title_vertical_info_services, EndPoints.URL_GET_VERTICAL_INFO_SERVICES)
        }
    }

    private fun btnTouristic() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vts_informative_turist).setOnClickListener {
            makeActivityImages(R.string.title_vertical_info_turistic, EndPoints.URL_GET_VERTICAL_INFO_TOURIST)
        }
    }

    private fun btnLocation() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vts_informative_location).setOnClickListener {
            makeActivityImages(R.string.title_vertical_info_localization, EndPoints.URL_GET_VERTICAL_INFO_LOCATION)
        }
    }

    private fun makeActivityImages(title: Int, url_img: String, code: Int = 1) {
        var cittusImage =
            CittisImage(resources.getString(title), url_img, code, ActionsRequest.GET_VERTICAL_IMAGES_VALUES)
        bundle.putParcelable("CittusImage", cittusImage)

        sendData(title)
    }

    private fun sendData(title: Int, idTypeSignal: Boolean = true) {
        if (idTypeSignal) {
            idSiganl = R.id.mainImage
        } else {
            idSiganl = R.id.verticalInformativeSignal
        }

        verticalNameSignal = when (title) {
            R.string.title_vertical_info_localization -> "Info. Localizacion"
            R.string.title_vertical_info_services -> "Info. Servicios"
            R.string.title_vertical_info_turistic -> "Info. Turismo"
            else -> ""
        }

        // Name Siganl Vertical
        var verticalSignal = VerticalSignals()
        verticalSignal.verticalNameSignal = verticalNameSignal
        setData(verticalSignal)
    }

    private fun setData(verticalSignal: VerticalSignals) {
        // Make Object - Cittis Signup
        var signsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.verticalSignal = verticalSignal
        // Reset
        signalArrayList[signalArrayList.size - 1] = signsUp
        // Add to DB
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)
        Navigation.findNavController(viewMain).navigate(idSiganl, bundle)
    }
}
