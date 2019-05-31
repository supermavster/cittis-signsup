package com.cittis.signsup.view.signal

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cittis.signsup.R
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.POSTAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import com.cittis.signsup.controller.images.upload.MakeToUpoad
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.Municipalities
import org.json.JSONObject
import java.io.File

class Finish : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()
    private var municipalities: Municipalities? = null
    // Make Clases
    var message = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_finish, container, false)
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
            if (it.municipality != null) {
                municipalities = it.municipality!!
            }
        }
        if (login.firebase_auth == 1) {
            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {
        // Btn Save Signal
        btnSave()
    }

    private fun btnSave() {
        viewMain.findViewById<Button>(R.id.btn_save_all).setOnClickListener {
            // Save Data
            saveAllElements()
        }
    }


    private fun saveAllElements() {
        if (municipalities != null && signalArrayList != null) {
            // Upload to Data Base
            uploadDataBase()
        } else {
            message = "Error faltal, no se pudo crear el inventario"
        }
        Snackbar.make(viewMain, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun uploadDataBase() {
        if (signalArrayList != null) {
            // Set Dates
            //postApiCall(EndPoints.URL_ADD_SIGNAL)
            Log.e("Full", cittisDB.toString())
            // Upload Images
            uploadImages()
        } else {
            message = "No se puede crear la señal, revise los datos por favor."
        }
    }


    private fun uploadImages() {
        var makeToUpoad: MakeToUpoad = MakeToUpoad(viewMain.context)
        var signal = signalArrayList[signalArrayList.size - 1]
        var image = signal.imagesByCode
        var path = image!!.pathImagen
        var file = File(path)
        var uri = Uri.fromFile(file)
        makeToUpoad.showFileChooser(uri)


    }


    /** Post Elements **/
    private fun postApiCall(url: String) {
        try {
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            val postApiRequest = POSTAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)

            var params = HashMap<String, String>()
            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("data", cittisDB.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            postApiRequest.request(viewMain.context, fetchPostResultListener, params, url)
            // Toast.makeText(viewMain.context, "POST API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for POST api request
    private var fetchPostResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {

            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our POSTAPIRequest class
                if (data.has("success")) {
                    val success = data.getInt("success")
                    if (success == 1) {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            //Display the result
                            //Or, You can do whatever you need to
                            //do with the JSONObject
                            Log.e("Data", response.toString(4))
                            message = "Datos añadidos con exito."

                            //if(response.getString("data")=="c
                            /*
                            // Upload Images
                            uploadImages()
                            // Reset Views contBaseID
                            */
                        }
                    } else {
                        message = "No se han podido subir los datos, reviselos por favor."

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
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }


}
