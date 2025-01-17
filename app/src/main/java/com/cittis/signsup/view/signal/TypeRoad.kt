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
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.GETAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import com.cittis.signsup.controller.plugins.ConvertJSON
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import org.json.JSONObject


class TypeRoad : Fragment() {

    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()

    private var maxSignal = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_type_road, container, false)
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
        // Set Data Count Signal
        getMaxIdSignals()
    }

    private fun getMaxIdSignals() {
        var url = EndPoints.URL_GET_MAX_SIGNAL(EndPoints.FireBaseID)
        getApiCall(url)
    }

    /** API - GET **/
    private fun getApiCall(url: String) {
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getapiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            getapiRequest.request(viewMain.context, fetchGetResultListener, url)
            //Toast.makeText(viewMain.context, "GET API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for GET api request
    private var fetchGetResultListener: FetchDataListener = object : FetchDataListener {


        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }

        override fun onFetchComplete(data: JSONObject) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our GETAPIRequest class
                if (data.has("success")) {
                    val success = data.getInt("success")
                    if (success == 1) {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            //Display the result
                            var array = response.toString(4)
                            var tempValue = ConvertJSON(array)["data"]
                            // Init Process
                            initProcessGet(tempValue)

                        }
                    } else {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            Log.e("Error", response.toString(4))
                            val error = response.getString("error")
                            RequestQueueService.showAlert(error, viewMain.context)
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                        }
                    }
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", viewMain.context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

    }

    private fun initProcessGet(values: Any) {
        maxSignal = values as Int
        if (maxSignal > 0) {
            viewMain.findViewById<Button>(R.id.btn_urban).setOnClickListener {
                setData("Urbana")
                Navigation.findNavController(viewMain).navigate(R.id.geolocalization, bundle)
            }

            viewMain.findViewById<Button>(R.id.btn_rural).setOnClickListener {
                setData("Rural")
                Navigation.findNavController(viewMain).navigate(R.id.typeSignal, bundle)
            }
        }
    }

    private fun setData(typeSignal: String) {

        // Make Object List Signal
        var signsUp = CittisSignsUp(maxSignal, typeSignal, null)
        signalArrayList.add(signsUp)
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)

    }



}
