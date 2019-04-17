package com.cittis.signsup.controller

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.cittis.signsup.R
import com.cittis.signsup.connection.DataBase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // Main Variables
    var mainActivity: Activity = this
    var maxID: String = ""

    // Data Base
    var connection: DataBase = DataBase(this)

    // Exception
    var exceptionMain: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Init Process (All Actions in the tab MAIN)
        initProcess()
    }

    private fun initProcess() {
        // Init Permissions
        // Permissions
        Permissions(this).setPermissions()
    }

    override fun onSupportNavigateUp() =
        findNavController(nav_host_fragment).navigateUp()


}
