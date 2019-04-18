package com.cittis.signsup.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cittis.signsup.R
import com.cittis.signsup.connection.DataBase

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Login.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Login : Fragment() {

    // Main Variables
    private lateinit var viewMain: View
    private lateinit var connection: DataBase
    // Make Bundle
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_login, container, false)

        // Init Process
        initProcess()

        // Return Data
        return viewMain
    }

    private fun initProcess() {

        // Data Base
        var connection: DataBase = DataBase(viewMain.context, this)
        connection.loginCall()
        viewMain.findViewById<Button>(R.id.btn_login).setOnClickListener {
            // Call the API

            //connection.postApiCall()
            connection.loginCall()
            Log.e("Data", connection.resultTextView.toString())
            Log.e("Data", connection.resultTextView.toString())
            Log.e("Data", connection.resultTextView.toString())
            Log.e("Data", connection.resultTextView.toString())
        }

    }

}
