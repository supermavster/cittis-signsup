package com.cittis.signsup.view.signal.vertical

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.CittisSignsUp
import com.cittis.signsup.model.DataUser
import com.cittis.signsup.model.VerticalSignals

class StatePost : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()
    // Variable Class
    var verticalSignal: VerticalSignals? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_state_post, container, false)
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
                // Variable Temp
                verticalSignal = checkSignal("Vertical") as VerticalSignals?

            }
        }
        if (login.firebase_auth == 1) {
            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {

        // Button Post
        buttonStay()
        // Save Data
        btnSave()
    }

    private fun buttonStay() {

        viewMain.findViewById<ImageButton>(R.id.ibtn_self_signal).setOnClickListener {
            viewMain.findViewById<RadioGroup>(R.id.rg_signal).check(R.id.rbtn_self_signal)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_light_signal).setOnClickListener {
            viewMain.findViewById<RadioGroup>(R.id.rg_signal).check(R.id.rbtn_light_signal)
        }

        viewMain.findViewById<ImageButton>(R.id.ibtn_wall_signal).setOnClickListener {
            viewMain.findViewById<RadioGroup>(R.id.rg_signal).check(R.id.rbtn_wall_signal)
        }

    }

    private fun btnSave() {
        viewMain.findViewById<Button>(R.id.btn_next_state_post).setOnClickListener {

            // Get Data
            var data = getData()
            verticalSignal!!.postTypeSignal = data[0]
            verticalSignal!!.statePost = 0f//data[1].toFloat()
            // Make Object Main
            setData(verticalSignal!!)
        }
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

        // Show data
        Navigation.findNavController(viewMain).navigate(R.id.photosGps, bundle)

    }

    fun getData(): ArrayList<String> {
        // TODO: Get Data Main - Information
        // 0 -> Size
        // 1 -> Starts - State Post
        var tempArray: ArrayList<String> = ArrayList<String>()
        var radioGroup = viewMain.findViewById<RadioGroup>(R.id.rg_signal)
        var rb = viewMain.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
        tempArray.add(0, rb.text.toString())

        // Todo Fix
        //tempArray.add(1, viewMain.findViewById<RatingBar>(R.id.ratingBar).rating.toString())
        return tempArray
    }


    // Complements
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
}
