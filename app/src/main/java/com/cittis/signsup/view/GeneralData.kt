package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.HorizontalSignals


class GeneralData : Fragment() {

    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser
    private var tempLocationSignal = ""


    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()

    // Variables temp
    var horizontalSignal: HorizontalSignals? = null

    // Variables Class
    // Variables
    var carril = ""
    var porcentaje = ""
    // Exception
    var exceptionMain: Boolean = false
    var elementsBaseImage = java.util.ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_general_data, container, false)
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
                horizontalSignal = checkSignal("Horizontal") as HorizontalSignals?

            }
        }
        if (login.firebase_auth == 1) {
            // Init Process
            initProcess()
        }
    }

    private fun checkSignal(type: String): Any? {
        var tempData: Any? = null
        var count = 0
        if (type == "Horizontal") {
            var size = signalArrayList.size
            for (i in 0 until size) {
                var tempObject = signalArrayList[i].horizontalSignal
                if (tempObject != null) {
                    count = i
                }
            }
            tempData = signalArrayList[count].horizontalSignal
        } else if (type == "Vertical") {
            var size = signalArrayList.size
            for (i in 0 until size) {
                var tempObject = signalArrayList[i].verticalSignal
                if (tempObject != null) {
                    count = i
                }
            }
            tempData = signalArrayList[count].verticalSignal
        }
        return tempData
    }

    private fun initProcess() {
        // Carril
        carril()

        // Save Button
        save()
    }


    private fun carril() {
        //Populate NumberPicker values from String array values
        var values = arrayOf(
            "Carril 1",
            "Carril 2",
            "Carril 3",
            "Carril 4",
            "Carril 5",
            "Carril 6"
        )

        var textPicker: NumberPicker = viewMain.findViewById<NumberPicker>(R.id.txt_location_signal)
        textPicker.minValue = 0
        textPicker.maxValue = values.size - 1
        textPicker.displayedValues = values
        textPicker.wrapSelectorWheel = true

        textPicker.setOnValueChangedListener { numberPicker, i, i1 ->
            carril = values[i1]
            Log.i("Selected Text : ", values[i1])
        }

        //Populate NumberPicker values from String array values
        var valTemp: ArrayList<String> = ArrayList<String>()
        for (i in 100 downTo 0 step 10) {
            valTemp.add("${i}%")
        }

        textPicker = viewMain.findViewById<NumberPicker>(R.id.txt_percentage_coverage)
        textPicker.minValue = 0
        textPicker.maxValue = valTemp.size - 1
        textPicker.displayedValues = valTemp.toTypedArray()
        textPicker.wrapSelectorWheel = true

        textPicker.setOnValueChangedListener { numberPicker, i, i1 ->
            porcentaje = valTemp.get(i1)
            Log.i("Selected Text : ", valTemp.get(i1))
        }
    }


    fun getDataMain(): ArrayList<String> {
        var tempValues = ArrayList<String>()
        // TODO: ISV Horizontal DATA MAIN
        // 1-0 -> Direccion
        // 1-1 -> Carril
        // 1-2 -> Porcentaje
        // 1-3 -> State
        try {
            // Direccion
            var rg_directional = viewMain.findViewById<RadioGroup>(R.id.rg_directional)
            var rb: RadioButton = viewMain.findViewById<RadioButton>(rg_directional.checkedRadioButtonId)
            tempValues.add(0, rb.text.toString()) // 1-0 -> Direccion

            // Carriles
            tempValues.add(1, carril) // 1-2 -> Carril

            tempValues.add(2, porcentaje) // 1-3 -> Porcentaje

            tempValues.add(3, viewMain.findViewById<RatingBar>(R.id.ratingBar).rating.toString())


            exceptionMain = false
        } catch (e: Exception) {
            Log.e("NULL", e.message)
            var errorMain = when {
                e.message.equals(
                    "findViewById(rg_directional.checkedRadioButtonId) must not be null",
                    true
                ) -> "Seleccione el Sentido de trafico"
                e.message.equals(
                    "Location was not Select",
                    true
                ) -> "Seleccione la Ubicación de la señal en el trayecto"
                else -> "ERROR: " + e.message
            }
            //Toast.makeText(this, "$errorMain  para poder continuar", Toast.LENGTH_SHORT).show()
            exceptionMain = true
        }

        return tempValues
    }

    private fun save() {
        viewMain.findViewById<Button>(R.id.btn_next_general_data).setOnClickListener {

            var dataTemp = getDataMain()
            // 1-0 -> Direccion
            // 1-1 -> Carril
            // 1-2 -> Porcentaje
            // 1-3 -> State

            horizontalSignal!!.directionJourney = dataTemp[0]
            horizontalSignal!!.rail = dataTemp[1]
            horizontalSignal!!.percentage = dataTemp[2]
            horizontalSignal!!.stateSingal = dataTemp[3].toFloat()
            setData(horizontalSignal!!)
        }
    }


    private fun setData(horizontalSignals: HorizontalSignals) {
        // Make Object - Cittis Signup
        var signsUp = signalArrayList[signalArrayList.size - 1]
        signsUp.horizontalSignal = horizontalSignals
        // Reset
        signalArrayList[signalArrayList.size - 1] = signsUp
        // Add to DB
        cittisDB.signal = signalArrayList
        // Show Data
        Log.e("Data-Login", cittisDB.toString())
        // Set and Send Data Main
        bundle.putParcelable("CittisDB", cittisDB)

        // Start Activity
        Navigation.findNavController(viewMain).navigate(R.id.photosGps, bundle)


    }

}
