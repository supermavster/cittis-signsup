package com.cittis.signsup.view.signal.horizontal

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

class HorizontalSignal : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_horizontal_signal, container, false)
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
        viewMain.findViewById<ImageButton>(R.id.ibtn_stretch).setOnClickListener {
            makeActivityImages(R.string.title_horizontal_stretch, EndPoints.URL_GET_HORIZONTAL_STRETCH)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_intersection).setOnClickListener {
            makeActivityImages(R.string.title_horizontal_intersection, EndPoints.URL_GET_HORIZONTAL_INTERSECTION)
        }

    }

    private fun makeActivityImages(title: Int, url_img: String, code: Int = 1) {

        var cittisImage =
            CittisImage(resources.getString(title), url_img, code, ActionsRequest.GET_HORIZONTAL_IMAGES_VALUES)
        Log.e("data", cittisImage.toString())
        bundle.putParcelable("CittisImage", cittisImage)

        var locationOnTheWay = if (title == R.string.title_horizontal_intersection) {
            "Intersección"
        } else {
            "Tramo"
        }

        // Location
        var horizontalSignal = HorizontalSignals()
        horizontalSignal.locationOnTheWay = locationOnTheWay

        // Add Data
        setData(horizontalSignal)
    }

    private fun setData(horizontalSignal: HorizontalSignals) {
        // Make Object - Cittis Signup
        var signsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.horizontalSignal = horizontalSignal
        // Reset
        signalArrayList[signalArrayList.size - 1] = signsUp
        // Add to DB
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)
        Navigation.findNavController(viewMain).navigate(R.id.mainImage, bundle)

    }
}
