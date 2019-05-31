package com.cittis.signsup.view.signal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.controller.images.MainImages
import com.cittis.signsup.model.*


class MainImage : Fragment() {
    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View

    // Make Bundle
    val bundle = Bundle()
    private lateinit var login: DataUser

    // Data Base
    private lateinit var cittisDB: CittisListSignal
    private var signalArrayList = ArrayList<CittisSignsUp>()


    // Variables locals - IMG
    private var imagenSelect: CittisImage? = null
    var mainImages: MainImages = MainImages()
    internal var elementsBase = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_main_image, container, false)

        return viewMain
    }

    // TODO: Get Data - Login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Image
        imagenSelect = arguments?.getParcelable("CittisImage")
        // Get data from last Fragment
        val someDataClass: CittisListSignal? = arguments?.getParcelable("CittisDB")
        cittisDB = someDataClass!!

        someDataClass.let {
            login = it.dataUser!!
            if (it.signal != null) {
                signalArrayList = it.signal!!
            }

        }
        if (login.firebase_auth == 1) {
            // Init Process
            initProcess()
        }
    }

    private fun initProcess() {
        // Actions
        actionsButtons()
        // Save elements
        saveElements()
    }

    private fun actionsButtons() {
        var viewPager = viewMain.findViewById<ViewPager>(R.id.viewPager)
        var SliderDots = viewMain.findViewById<LinearLayout>(R.id.SliderDots)
        var spinCode = viewMain.findViewById<Spinner>(R.id.spin_code_images)

        // Start Process
        mainImages.getValuesMain(imagenSelect!!, viewMain.context, fragment, viewPager, SliderDots, spinCode)

        // Active Select
        viewMain.findViewById<LinearLayout>(R.id.codeMain).visibility =
            View.VISIBLE//findViewById(R.id.codeMain).setVisibility(intent.getBooleanExtra("code", true) ? View.VISIBLE : View.GONE);

        // Set Template New Activity
        viewMain.findViewById<TextView>(R.id.lbl_title_images).text = imagenSelect!!.title

    }

    private fun saveElements() {
        viewMain.findViewById<Button>(R.id.btn_save_image).setOnClickListener {
            // TODO: Get Data Images
            // 0 -> Number
            // 1 -> Code
            // 2 -> Img Select
            if (mainImages.spinner != null) {
                val ss = mainImages.spinner!!.selectedItem.toString()
                var arrayCodes = mainImages.arrayCodes
                var arrayImages = mainImages.arrayImages
                for (i in arrayCodes.indices) {
                    if (arrayCodes[i] === ss) {
                        elementsBase.add(0, i.toString())
                        elementsBase.add(1, arrayCodes[i])
                        elementsBase.add(2, arrayImages[i])
                    }
                }
                // Array Add Images
                var imagesByCode: ImagenSignalCode = ImagenSignalCode()
                imagesByCode.idImagen = elementsBase[0].toInt()
                imagesByCode.codeImagen = elementsBase[1]
                imagesByCode.pathImagen = elementsBase[2]

                // Make Object Main
                // Make Object - Cittis Signup
                var signsUp = signalArrayList[signalArrayList.size - 1]
                signsUp.imagesByCode = imagesByCode
                // Reset
                signalArrayList[signalArrayList.size - 1] = signsUp
                // Add to DB
                cittisDB.signal = signalArrayList
                // Show Data
                Log.e("Data-Login", cittisDB.toString())
                // Set and Send Data Main
                bundle.putParcelable("CittisDB", cittisDB)
                Navigation.findNavController(viewMain).navigate(R.id.addressLocation, bundle)
            }
        }
    }
}
