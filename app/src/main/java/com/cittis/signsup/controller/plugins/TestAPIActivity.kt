package com.cittis.signsup.controller.plugins

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cittis.signsup.R
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.GETAPIRequest
import com.cittis.signsup.connection.POSTAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import org.json.JSONObject


class TestAPIActivity : Fragment() {

    private var resultTextView: TextView? = null
    private lateinit var viewMain: View
    private var fragment = this


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.activity_test_api, container, false)



        resultTextView = viewMain.findViewById(R.id.resultTextView)

        viewMain.findViewById<Button>(R.id.getApiBtn).setOnClickListener {
            getApiCall("http://192.168.0.14/app?departments=all&idFirebase=LJCpaE6xqmTOmtsKXJp6hnnya2d2")
        }

        viewMain.findViewById<Button>(R.id.postApiBtn).setOnClickListener {
            //postApiCall()
        }

        return viewMain
    }

    private fun getApiCall(url: String) {
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getapiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            getapiRequest.request(viewMain.context, fetchGetResultListener, url)
            Toast.makeText(viewMain.context, "GET API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for GET api request
    private var fetchGetResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our GETAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        val success = data.getInt("success")
                        if (success == 1) {
                            val response = data.getJSONObject("response")
                            if (response != null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                resultTextView!!.text = response.toString(4)
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", viewMain.context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }

    private fun postApiCall(url: String) {
        try {
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            val postapiRequest = POSTAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)
            var params = HashMap<String, String>()

            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("userId", "2")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            postapiRequest.request(viewMain.context, fetchPostResultListener, params, url)
            Toast.makeText(viewMain.context, "POST API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for POST api request
    private var fetchPostResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our POSTAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        val success = data.getInt("success")
                        if (success == 1) {
                            val response = data.getJSONObject("response")
                            if (response != null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                resultTextView!!.text = response.toString(4)
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", viewMain.context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }
}
