package com.cittis.signsup.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cittis.signsup.DisplayActivity
import com.cittis.signsup.R
import com.cittis.signsup.connection.DataBase


class Municipalities : Fragment() {

    // Main Variables
    private lateinit var viewMain: View
    private lateinit var dataBase: DataBase

    // Make Bundle
    val bundle = Bundle()
    var login = 0


    var maxIDInventario = ""
    var maxIDListSignal = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_municipalities, container, false)
        // Init Connection
        //connection = DAOConnection(viewMain.context)
        viewMain.findViewById<Button>(R.id.btnNotify).setOnClickListener {
            val intent = Intent(viewMain.context, DisplayActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
        return viewMain
    }

}
