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

class VerticalSignal : Fragment() {
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
        viewMain = inflater.inflate(R.layout.fragment_vertical_signal, container, false)
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
        // BTN Information
        btnInformation()
        // BTN Regulatory
        btnRegulatory()
        // BTN Preventive
        btnPreventive()
        // BTN Work
        btnWork()
        // BTN Cycle Route
        btnCycleRoute()
    }

    private fun btnInformation() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vertical_informative).setOnClickListener { view ->
            sendData(0, false)
        }
    }

    private fun btnRegulatory() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vertical_regulatory).setOnClickListener { view ->
            makeActivityImages(R.string.title_vertical_regulatory, EndPoints.URL_GET_VERTICAL_REGULATORY)
        }
    }

    private fun btnPreventive() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vertical_preventive).setOnClickListener { view ->
            makeActivityImages(R.string.title_vertical_preventives, EndPoints.URL_GET_VERTICAL_PREVENTIVES)
        }
    }

    private fun btnWork() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vertical_work).setOnClickListener { view ->
            makeActivityImages(R.string.title_vertical_work, EndPoints.URL_GET_VERTICAL_WORK)
        }
    }

    private fun btnCycleRoute() {
        viewMain.findViewById<ImageButton>(R.id.ibtn_vertical_cycle_route).setOnClickListener { view ->
            makeActivityImages(R.string.title_vertical_cycle_route, EndPoints.URL_GET_VERTICAL_CYCLE_ROUTE)
        }
    }

    private fun makeActivityImages(title: Int, url_img: String, code: Int = 1) {
        var cittisImage =
            CittisImage(resources.getString(title), url_img, code, ActionsRequest.GET_VERTICAL_IMAGES_VALUES)
        Log.e("data", cittisImage.toString())
        bundle.putParcelable("CittisImage", cittisImage)

        sendData(title)
    }

    private fun sendData(title: Int, idTypeSignal: Boolean = true) {
        if (idTypeSignal) {
            idSiganl = R.id.mainImage
        } else {
            idSiganl = R.id.verticalInformativeSignal
        }

        verticalNameSignal = when (title) {
            R.string.title_vertical_regulatory -> "Reglamentaria"
            R.string.title_vertical_preventives -> "Preventiva"
            R.string.title_vertical_work -> "Obra"
            R.string.title_vertical_cycle_route -> "Cicloruta"
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
