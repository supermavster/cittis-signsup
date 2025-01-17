package com.cittis.signsup.view.signal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.model.CittisImage
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser

class TypeSignal : Fragment() {

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
        viewMain = inflater.inflate(R.layout.fragment_type_signal, container, false)
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
        viewMain.findViewById<Button>(R.id.btn_horizontal).setOnClickListener {
            setData("Horizontal")
            Navigation.findNavController(viewMain).navigate(R.id.horizontalSignal, bundle)
        }

        viewMain.findViewById<Button>(R.id.btn_vertical).setOnClickListener {
            setData("Vertical")
            Navigation.findNavController(viewMain).navigate(R.id.verticalSignal, bundle)
        }

        viewMain.findViewById<Button>(R.id.btn_others).setOnClickListener {
            setData("Another")
            var cittisImage =
                CittisImage(
                    resources.getString(R.string.title_another),
                    EndPoints.URL_GET_ANOTHER,
                    1,
                    ActionsRequest.GET_HORIZONTAL_IMAGES_VALUES
                )
            Log.e("data", cittisImage.toString())
            bundle.putParcelable("CittisImage", cittisImage)

            Navigation.findNavController(viewMain).navigate(R.id.mainImage, bundle)
        }


    }

    private fun setData(typeSignal: String) {
        // Make Object - Cittis Signup
        var signsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.typeSignal = typeSignal
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
