package com.cittis.signsup.connection

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.actions.FetchDataListener
import org.json.JSONObject

class DataBase(// Context
    private var context: Context, private var fragment: Fragment
) {

    var resultTextView: String? = ""

    // Usse: postApiListener or
    //Call getApiCall() method
    // getApiCall()

    //Call postApiCall() method
    // postApiCall()


    /** Get Elements **/
    //Implementing interfaces of FetchDataListener for GET api request
    internal var fetchGetResultListener: FetchDataListener = object : FetchDataListener {
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
                                resultTextView = response.toString(4)
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", context)
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", context)
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

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, context)
        }
    }


    /** Post Elements **/
    //Implementing interfaces of FetchDataListener for POST api request
    internal var fetchPostResultListener: FetchDataListener = object : FetchDataListener {
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
                                resultTextView = response.toString(4)
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", context)
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", context)
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg, context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, context)
        }
    }


    private fun getApiCall() {
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getapiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            val url = "webapi.php?userId=1"
            getapiRequest.request(context, fetchGetResultListener, url)
            Toast.makeText(context, "GET API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun postApiCall() {
        try {
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            val postapiRequest = POSTAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)

            //base server URL
            val baseUrl = "http://studypeek.com/test/"
            //add extension api url received from caller
            //and make full api
            val url = baseUrl + "webapi.php"
            val params = JSONObject()
            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("userId", 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            postapiRequest.request(context, fetchPostResultListener, params, url)

            Toast.makeText(context, "Post API called $resultTextView", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /** Login Elements **/
    fun loginCall() {
        try {
            //Create Instance of POSTAPIRequest and call-
            var url = EndPoints.URL_CHECK_SIGIN
            //Creating POST body in JSON format
            //to send in POST request
            url += ("?userAcceso=as")
            url += "&passAcceso=Mavster.1"

            Log.e("data", url)

            val getapiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            getapiRequest.request(context, fetchGetResultListener, url)
            Toast.makeText(context, "GET API called $resultTextView", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



}