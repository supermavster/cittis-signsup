package com.cittis.signsup.view.signal

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
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


    private fun initProcess() {


        //Attaching only part of URL as base URL is given
        //in our GETAPIRequest(of course that need to be same for all case)
        // Lock Button and TextView
        viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = false
        viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = false


        var url = EndPoints.URL_GET_DEPARTMENTS_BY_IDFIREBASE("all", EndPoints.FireBaseID)
        Log.e("data", url)
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

        viewMain.findViewById<Button>(R.id.btn_salir).setOnClickListener {
            // [START config_signin]
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            // [END config_signin]

            var googleSignInClient = GoogleSignIn.getClient(viewMain.context, gso)


            // [START initialize_auth]
            // Initialize Firebase Auth
            var auth = FirebaseAuth.getInstance()
            // [END initialize_auth]

            // TODO: SALIR
            auth.signOut()
            // Google sign out
            googleSignInClient.signOut().addOnCompleteListener {
                System.exit(0)
            }
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
                            //Or, You can do whatever you need to do with the JSONObject
                            Log.e("Data", response.toString(4))

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

        var id = when (idObject) {
            "departments" -> R.id.auto_complete_departamento
            "municipalities" -> R.id.auto_complete_municipio
            else -> 0

        }

        var arrayTemp = JsonUtil2.getStringListFromJsonArray(values as JSONArray)
        // Make Elements
        makeAutocomplete(
            arrayTemp as ArrayList<String>,
            viewMain.findViewById<AutoCompleteTextView>(id)
        )
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
                when (idObject) {
                    "departments" -> {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        // Display the clicked item using toast
                        // Active Buttons
                        viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = true
                        viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = true
                        // Add Actionsion(position).toString()
                        val url = EndPoints.URL_GET_MUNICIPALITIES_BY_IDFIREBASE(selectedItem, EndPoints.FireBaseID)
                        //Log.e("Data", url)
                        getApiCall(url, "municipalities")
                        //Toast.makeText(context,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
                    }
                    "municipalities" -> {
                        hideKeyboard(viewMain)
                    }
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

    fun hideKeyboard(view: View) {
        val imm = viewMain.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
