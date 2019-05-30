package com.cittis.signsup.controller.images

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.RequestQueue
import com.cittis.signsup.R
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.GETAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import com.cittis.signsup.controller.images.slider.CustomVolleyRequest
import com.cittis.signsup.controller.images.slider.ViewPagerAdapter
import com.cittis.signsup.controller.plugins.ConvertJSON
import com.cittis.signsup.controller.plugins.JsonUtil2
import com.cittis.signsup.model.CittisImage
import com.cittis.signsup.model.SliderUtils
import org.json.JSONArray
import org.json.JSONObject


// TODO: Cambiar a Fragment
class MainImages {

    // Variables Globals
    var spinner: Spinner? = null
    var sliderOn: Boolean? = false

    // Variables locals - IMG
    var viewPager: ViewPager? = null
    var sliderDotspanel: LinearLayout? = null
    private var dotscount: Int = 0
    private var dots: ArrayList<ImageView> = ArrayList<ImageView>()
    private var imgURL: String? = null
    var rq: RequestQueue? = null
    var sliderImg: MutableList<SliderUtils>? = null
    var viewPagerAdapter: ViewPagerAdapter? = null

    var arrayCodes: ArrayList<String> = ArrayList<String>()
    var arrayImages: ArrayList<String> = ArrayList<String>()

    lateinit var fragment: Fragment
    lateinit var context: Context

    internal var actionImages = 5
    internal var titleMain = ""
    internal var request_url: String? = null


    private fun intiProcess() {
        // Make Slider
        request_url?.let { getApiCall(it) }

        // Set Process to Click
        processClick()
    }

    fun getValuesMain(
        imageSelect: CittisImage,
        context: Context,
        fragment: Fragment,
        viewPagerTemp: ViewPager,
        SliderDotsTemp: LinearLayout,
        spinCode: Spinner
    ) {

        // Select Values
        val titleMain = imageSelect.title
        val urlImg = imageSelect.url_img
        val action = imageSelect.Action

        // Fragment
        this.fragment = fragment

        // Set Variables
        this.titleMain = titleMain//;intent.getStringExtra("title");
        request_url = urlImg//intent.getStringExtra("url_img");
        actionImages = action//intent.getIntExtra("Action",5);

        // Init Varaibles Main

        arrayCodes = ArrayList<String>()
        arrayImages = ArrayList<String>()
        // Init Elements Base (Consult and Set)
        sliderImg = ArrayList()
        this.context = context

        rq = CustomVolleyRequest.getInstance(this.context).requestQueue

        // Init Elements - View
        viewPager = viewPagerTemp//(ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = SliderDotsTemp//(LinearLayout) findViewById(R.id.SliderDots);

        // Selection of the spinner
        spinner = spinCode//(Spinner) findViewById(R.id.spin_code_images);

        // Init Process
        intiProcess()
    }

    private fun processClick() {
        // Call the window of Main Image
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            // Set selected Action
            override fun onPageSelected(position: Int) {
                sliderOn = true
                setElementByPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        // Spinner Actions

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                // TODO Auto-generated method stub

                val ss = spinner?.selectedItem.toString()
                for (i in arrayCodes.indices) {
                    if (arrayCodes[i] === ss) {
                        /*
                            int positionTemp = (i);
                            String imageUrl = arrayImages[positionTemp];
                            sliderImg = new ArrayList<>();
                            SliderUtils sliderUtils = new SliderUtils();
                            sliderUtils.setSliderImageUrl(imageUrl);
                            sliderImg.add(sliderUtils);
                            setSlider();*/
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub
            }
        }
    }


    /** Call Api - GET */
    private fun getApiCall(url: String) {


        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getApiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            getApiRequest.request(context, fetchGetResultListener, url)
            // Toast.makeText(viewMain.context, "GET API called: $case", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for GET api request
    private var fetchGetResultListener: FetchDataListener = object : FetchDataListener {

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, context)
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
                            var tempValue: JSONObject = ConvertJSON(array)
                            // Init Process
                            initProcessGet(tempValue)
                        }
                    } else {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            Log.e("Error", response.toString(4))
                            val error = response.getString("error")
                            RequestQueueService.showAlert(error, context)
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", context)
                        }
                    }
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", context)
                e.printStackTrace()
            }

        }


        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, context)
        }


    }


    private fun initProcessGet(values: JSONObject) {

        val data: JSONArray = values["data"] as JSONArray
        val response: List<Any> = JsonUtil2.getListFromJsonArray(data)
        // Get And Set Data from te JSON
        for (i in 0 until response.size) {

            var listObjects = response[i] as List<Any>

            var sliderUtils = SliderUtils()
            for (j in 0 until listObjects.size) {
                var objectTemp = listObjects[j] as List<Any>
                Log.e("d", objectTemp[1].toString())

                // Get And Set Data from te JSON
                when (j) {
                    0 -> {
                        sliderUtils = SliderUtils()
                        arrayCodes.add(objectTemp[1].toString())
                    }
                    1 -> arrayImages.add(objectTemp[1].toString())
                    2 -> {
                        imgURL = objectTemp[1].toString()
                        // Set Imagen parameter
                        sliderUtils.sliderImageUrl = imgURL as String
                        // Add Object to the Array
                        sliderImg!!.add(sliderUtils)
                    }

                }

            }

        }

        // Set Elements Get To the Slider
        setSlider()
        // Set Elements Get to the Spinner
        setSpinner()
    }


    private fun setSlider() {
        // Init Elements Base - Adapters
        viewPagerAdapter = ViewPagerAdapter(sliderImg, context, spinner)
        viewPager!!.adapter = viewPagerAdapter
        // Get Count
        dotscount = viewPagerAdapter!!.count
        // Check Elements
        if (dotscount > 0) {
            // Make Dots by Count
            dots = ArrayList<ImageView>()
            for (i in 0 until dotscount) {
                // Set location and move to do it
                var tempImg = ImageView(context)
                tempImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot))
                // Add data
                dots.add(tempImg)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                sliderDotspanel!!.addView(dots[i], params)
            }
            // Set image at select the dot
            dots[0].setImageDrawable(ContextCompat.getDrawable(context, com.cittis.signsup.R.drawable.active_dot))
        }
    }

    private fun setSpinner() {
        // Application of the Array to the Spinner
        val spinnerArrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayCodes)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        spinner!!.adapter = spinnerArrayAdapter
    }

    private fun setElementByPosition(position: Int) {
        // Move another Elements (DOTS)
        for (i in 0 until dotscount) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot))
        }
        // Set Image
        dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot))
        // Set element in Spinner
        if (position >= 0) {
            spinner!!.setSelection(position)
        }
    }


}