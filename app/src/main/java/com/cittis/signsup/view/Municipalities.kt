package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cittis.signsup.R
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


    var maxIDInventario = ""
    var maxIDListSignal = ""


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
        someDataClass?.let {
            login = it.dataUser!!
        }
        if (login.firebase_auth === 1) {
            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {
        /** TODO: TRACKING (ALL Users)
        viewMain.findViewById<Button>(R.id.btnNotify).setOnClickListener {
        val intent = Intent(viewMain.context, DisplayActivity::class.java)
        // start your next activity
        startActivity(intent)
        }*/
    }

    private fun saveAndNext() {
        /*
        var data = ArrayList<String>()
            // TODO: Location DATA MAIN (1)
            // 0 -> Id Inventario
            // 1 -> Id Lista Senal
            // 2 -> Municipio
            // 3 -> Departamento
            // 4 -> Id Max Signal

            // Get Max Inventario
            if (maxIDInventario.isEmpty()) {
                maxIDInventario = connection.getDataSingle(EndPoints.URL_GET_MAX_ID + "inventario")
            }
            data.add(0, maxIDInventario) // 0 -> Id Inventario
            // Get Max Signal
            if (maxIDListSignal.isEmpty()) {
                maxIDListSignal = connection.getDataSingle(EndPoints.URL_GET_MAX_ID + "lista")
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
            data.add(4, maxIDListSignal)//connection.loadElement(EndPoints.URL_GET_MAX_ID+"senal"))

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
            var cittusDB: CittusListSignal = CittusListSignal(login, municipalities, null, null)
            // Show Data
            Log.e("Data-Municipalities", cittusDB.toString())
            // Set and Send Data Main
            bundle.putParcelable("CittusDB", cittusDB)
            // Init Action
            Navigation.findNavController(viewMain).navigate(R.id.geolocalizationActivity, bundle)

        }


         */
    }

}
