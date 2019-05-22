package com.cittis.signsup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView


class TestAPIActivity : AppCompatActivity() {

    private var resultTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_api)
    }
    /*   resultTextView = findViewById(R.id.resultTextView)

       findViewById<Button>(R.id.getApiBtn).setOnClickListener {
           getApiCall("http://192.168.0.14/app?departments=all&idFirebase=LJCpaE6xqmTOmtsKXJp6hnnya2d2")
       }

       findViewById<Button>(R.id.postApiBtn).setOnClickListener {
           //postApiCall()
       }

   }

   private fun getApiCall(url: String) {
       try {
           //Create Instance of GETAPIRequest and call it's
           //request() method
           val getapiRequest = GETAPIRequest()
           //Attaching only part of URL as base URL is given
           //in our GETAPIRequest(of course that need to be same for all case)
           getapiRequest.request(this@TestAPIActivity, fetchGetResultListener, url)
           Toast.makeText(this@TestAPIActivity, "GET API called", Toast.LENGTH_SHORT).show()
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
                           RequestQueueService.showAlert("Error! No data fetched", this@TestAPIActivity)
                       }
                   }
               } else {
                   RequestQueueService.showAlert("Error! No data fetched", this@TestAPIActivity)
               }
           } catch (e: Exception) {
               RequestQueueService.showAlert("Something went wrong", this@TestAPIActivity)
               e.printStackTrace()
           }

       }

       override fun onFetchFailure(msg: String) {
           RequestQueueService.cancelProgressDialog()
           //Show if any error message is there called from GETAPIRequest class
           RequestQueueService.showAlert(msg, this@TestAPIActivity)
       }

       override fun onFetchStart() {
           //Start showing progressbar or any loader you have
           RequestQueueService.showProgressDialog(this@TestAPIActivity)
       }
   }

   private fun postApiCall(url: String) {
       try {
           //Create Instance of POSTAPIRequest and call it's
           //request() method
           val postapiRequest = POSTAPIRequest()
           //Attaching only part of URL as base URL is given
           //in our POSTAPIRequest(of course that need to be same for all case)
           val params = JSONObject()
           try {
               //Creating POST body in JSON format
               //to send in POST request
               params.put("userId", 2)
           } catch (e: Exception) {
               e.printStackTrace()
           }

           postapiRequest.request(this@TestAPIActivity, fetchPostResultListener, params, url)
           Toast.makeText(this@TestAPIActivity, "POST API called", Toast.LENGTH_SHORT).show()
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
                           RequestQueueService.showAlert("Error! No data fetched", this@TestAPIActivity)
                       }
                   }
               } else {
                   RequestQueueService.showAlert("Error! No data fetched", this@TestAPIActivity)
               }
           } catch (e: Exception) {
               RequestQueueService.showAlert("Something went wrong", this@TestAPIActivity)
               e.printStackTrace()
           }

       }

       override fun onFetchFailure(msg: String) {
           RequestQueueService.cancelProgressDialog()
           //Show if any error message is there called from POSTAPIRequest class
           RequestQueueService.showAlert(msg, this@TestAPIActivity)
       }

       override fun onFetchStart() {
           //Start showing progressbar or any loader you have
           RequestQueueService.showProgressDialog(this@TestAPIActivity)
       }
   }*/
}
