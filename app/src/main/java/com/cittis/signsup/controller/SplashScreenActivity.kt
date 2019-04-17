package com.cittis.signsup.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.cittis.signsup.R

class SplashScreenActivity : AppCompatActivity() {

    // Time out - Splash [Seconds]
    private val SPLASH_TIME_OUT:Long=3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Init process
        initProcess()
    }

    private fun initProcess(){
        // This method will be executed once the timer is over
        Handler().postDelayed({
            // Start your app main activity
            startActivity(Intent(this, MainActivity::class.java))
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}
