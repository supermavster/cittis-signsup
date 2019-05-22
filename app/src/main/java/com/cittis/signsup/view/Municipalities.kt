package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.GETAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import com.cittis.signsup.controller.plugins.ConvertJSON
import com.cittis.signsup.controller.plugins.JsonUtil2
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.Municipalities
import org.json.JSONArray
import org.json.JSONObject


class Municipalities : Fragment() {

    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View
    // private lateinit var dataBase: DataBase

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal


    var data = arrayListOf<String>()

    var idObject = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_municipalities, container, false)


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

    private fun initProcesss() {
        /** TODO: TRACKING (ALL Users)
        viewMain.findViewById<Button>(R.id.btnNotify).setOnClickListener {
        val intent = Intent(viewMain.context, DisplayActivity::class.java)
        // start your next activity
        startActivity(intent)
        }*/
    }


    private fun initProcess() {

        /* TODO: END PROCESS/ Get Max Inventario
        if (idProject.isEmpty()) {

        }
        data.add(idProject) // 0 -> Id Inventario
        // Get Max Signal
        if (maxSignal.isEmpty()) {
            var url = EndPoints.URL_GET_MAX_SIGNAL(login.firebase_id.toString())
            Log.e("data",url)
            //getApiCall(url, "maxSignal");
        }
        data.add(maxSignal) // 1 -> Id Lista Senal*/

        //Attaching only part of URL as base URL is given
        //in our GETAPIRequest(of course that need to be same for all case)
        // Lock Button and TextView
        viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = false
        viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = false


        var url = EndPoints.URL_GET_DEPARTMENTS_BY_IDFIREBASE("all", login.firebase_id.toString())
        getApiCall(url, "departments")
        viewMain.findViewById<Button>(R.id.buttonMain).setOnClickListener { view ->

            // TODO: Location DATA MAIN (1)
            // 0 -> Municipio
            // 1 -> Departamento

            // Get Municipio
            data.add(
                viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).text.toString()
            )

            // Get Departamento
            data.add(
                viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_departamento).text.toString()
            )


            // Set Data
            // 0 -> Municipio
            // 1 -> Departamento
            var nameMunicipal = data[0]
            var nameDepartment = data[1]

            Log.e("data", data.toString())

            // Make Object Municipalities
            val municipalities = Municipalities(nameMunicipal, nameDepartment)
            // Make Object Main
            cittisDB.municipality = municipalities
            // Show Data
            Log.e("Data-Login", cittisDB.toString())
            // Set and Send Data Main
            bundle.putParcelable("CittisDB", cittisDB)
            // Init Action
            Navigation.findNavController(viewMain).navigate(R.id.typeRoad, bundle)

        }
    }


    /** Call Api - GET */
    private fun getApiCall(url: String, case: String) {
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getApiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            idObject = case
            getApiRequest.request(viewMain.context, fetchGetResultListener, url)
            // Toast.makeText(viewMain.context, "GET API called: $case", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for GET api request
    private var fetchGetResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our GETAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        val success = data.getInt("success")
                        if (success == 1) {
                            val response = data.getJSONObject("response")
                            if (response != null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                var array = response.toString(4)
                                var tempValue = ConvertJSON(array)["data"]

                                var id = when (idObject) {
                                    "departments" -> R.id.auto_complete_departamento
                                    "municipalities" -> R.id.auto_complete_municipio
                                    else -> 0

                                }

                                var arrayTemp = JsonUtil2.getStringListFromJsonArray(tempValue as JSONArray)
                                // Make Elements
                                makeAutocomplete(
                                    arrayTemp as ArrayList<String>,
                                    viewMain.findViewById<AutoCompleteTextView>(id)
                                )

                            }
                        } else {
                            RequestQueueService.showAlert(
                                "Error! No data fetched",
                                viewMain.context
                            )
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
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

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }

    /**  */
    private fun makeAutocomplete(array: ArrayList<String>, auto_complete_text_view: AutoCompleteTextView) {


        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            context, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            array // Array
        )

        // Set the AutoCompleteTextView adapter
        auto_complete_text_view.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        auto_complete_text_view.threshold = 1


        // Set an item click listener for auto complete text view
        auto_complete_text_view.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if (idObject == "departments") {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    // Display the clicked item using toast
                    // Active Buttons
                    viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = true
                    viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = true
                    // Add Actionsion(position).toString()
                    val url = EndPoints.URL_GET_MUNICIPALITIES_BY_IDFIREBASE(selectedItem, login.firebase_id.toString())
                    //Log.e("Data", url)
                    getApiCall(url, "municipalities")
                    //Toast.makeText(context,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
                }
            }


        // Set a dismiss listener for auto complete text view
        auto_complete_text_view.setOnDismissListener {
            //Toast.makeText(applicationContext,"Suggestion closed.", Toast.LENGTH_SHORT).show()
        }


        // Set a focus change listener for auto complete text view
        auto_complete_text_view.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                auto_complete_text_view.showDropDown()
            }
        }
    }


}
