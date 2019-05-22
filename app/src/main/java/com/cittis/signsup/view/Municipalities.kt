package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.cittis.signsup.R
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.connection.DataBase
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.DataUser


class Municipalities : Fragment() {

    // Main Variables
    private lateinit var viewMain: View
    private lateinit var dataBase: DataBase

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittusDB: CittisListSignal

    var maxIDInventario = ""
    var maxIDListSignal = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_municipalities, container, false)

        // Init DB
        dataBase = DataBase(viewMain.context, this)

        return viewMain
    }

    // TODO: Get Data - Login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get data from last Fragment
        val someDataClass: CittisListSignal? = arguments?.getParcelable("CittisDB")
        cittusDB = someDataClass!!

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
        //Attaching only part of URL as base URL is given
        //in our GETAPIRequest(of course that need to be same for all case)
        // Lock Button and TextView
        viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = false
        viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = false


        var urlArrayDepartments = EndPoints.URL_GET_DEPARTMENTS_BY_IDFIREBASE("all", login.firebase_id.toString())
        //dataBase.getApiCall(urlArrayDepartments);
        Log.e("Data", "URL: $urlArrayDepartments")


        /*


        // Departamento
        var array = dataBase.getData(EndPoints.URL_GET_DEPARTAMENTOS)
        makeAutocomplete(array, viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_departamento))

        // Set Actions
        viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_departamento).onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Active Buttons
                viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).isEnabled = true
                viewMain.findViewById<Button>(R.id.buttonMain).isEnabled = true
                // Add Actions
                val selectedItem = parent.getItemAtPosition(position).toString()
                array = dataBase.getData(EndPoints.URL_GET_SEARCH_MUNICIPIOS + selectedItem)
                makeAutocomplete(array, viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio))
            }


        viewMain.findViewById<Button>(R.id.buttonMain).setOnClickListener { view ->
            var data = ArrayList<String>()
            // TODO: Location DATA MAIN (1)
            // 0 -> Id Inventario
            // 1 -> Id Lista Senal
            // 2 -> Municipio
            // 3 -> Departamento
            // 4 -> Id Max Signal

            // Get Max Inventario
            if (maxIDInventario.isEmpty()) {
                maxIDInventario = dataBase.getDataSingle(EndPoints.URL_GET_MAX_ID + "inventario")
            }
            data.add(0, maxIDInventario) // 0 -> Id Inventario
            // Get Max Signal
            if (maxIDListSignal.isEmpty()) {
                maxIDListSignal = dataBase.getDataSingle(EndPoints.URL_GET_MAX_ID + "lista")
            }
            data.add(1, maxIDListSignal) // 1 -> Id Lista Senal

            // Get Municipio
            data.add(
                2,
                viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_municipio).text.toString()
            ) // 2 -> Municipio

            // Get Municipio
            data.add(
                3,
                viewMain.findViewById<AutoCompleteTextView>(R.id.auto_complete_departamento).text.toString()
            ) // 3 -> Departamento

            // Get Max Id Singal
            data.add(4, maxIDListSignal)//dataBase.loadElement(EndPoints.URL_GET_MAX_ID+"senal"))

            // Set Data
            // 0 -> Id Inventario
            // 1 -> Id Lista Senal
            // 2 -> Municipio
            // 3 -> Departamento
            // 4 -> Id Max Signal
            var idInventario = data.get(0).toInt()
            var idListSignal = data.get(1).toInt()
            var idMaxSignal = data.get(4).toInt()
            var nameMunicipal = data.get(2)
            var nameDepartment = data.get(3)

            // Make Object Municipalities
            val municipalities = Municipalities(idInventario, idListSignal, idMaxSignal, nameMunicipal, nameDepartment)
            // Make Object Main
            //var cittusDB: CittusListSignal = CittusListSignal(login, municipalities, null, null)
            //cittusDB
            // Show Data
            Log.e("Data-Municipalities", cittusDB.toString())
            // Set and Send Data Main
            bundle.putParcelable("CittusDB", cittusDB)
            // Init Action
            //Navigation.findNavController(viewMain).navigate(R.id.geolocalizationActivity, bundle)

        }
        */
    }


    private fun makeAutocomplete(array: ArrayList<String>, auto_complete_text_view: AutoCompleteTextView) {

/*
        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            context , // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            array // Array
        )

        // Set the AutoCompleteTextView adapter
        auto_complete_text_view.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        auto_complete_text_view.threshold = 1


        // Set an item click listener for auto complete text view
        auto_complete_text_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            //Toast.makeText(applicationContext,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
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
        */
    }
}