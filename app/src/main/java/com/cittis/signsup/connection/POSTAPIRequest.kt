package com.cittis.signsup.connection

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.cittis.signsup.actions.FetchDataListener
import org.json.JSONException
import org.json.JSONObject


class POSTAPIRequest {
    @Throws(JSONException::class)
    fun request(context: Context, listener: FetchDataListener?, parameters: HashMap<String, String>, ApiURL: String) {
        listener?.onFetchStart()

        val stringRequest = object : StringRequest(Request.Method.POST, ApiURL,
            Response.Listener<String> { obj ->
                try {
                    val response = JSONObject(obj)
                    //Log.e("response", response.toString())
                    if (listener != null) {
                        if (response.has("response")) {
                            //received response
                            //call onFetchComplete of the listener
                            listener.onFetchComplete(response)
                        } else if (response.has("error")) {
                            //has error in response
                            //call onFetchFailure of the listener
                            listener.onFetchFailure(response.getString("error"))
                        } else {
                            listener.onFetchComplete(null!!)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                if (error is NoConnectionError) {
                    listener!!.onFetchFailure("Network Connectivity Problem")
                } else if (error.networkResponse != null && error.networkResponse.data != null) {
                    val volley_error = VolleyError(String(error.networkResponse.data))
                    var errorMessage = ""
                    try {
                        Log.e("da", volley_error.toString())
                        //val errorJson = JSONObject(volley_error.message.toString())
                        //if (errorJson.has("error")) errorMessage = errorJson.getString("error")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (errorMessage.isEmpty()) {
                        errorMessage = volley_error.message.toString()
                    }

                    listener?.onFetchFailure(errorMessage)
                } else {
                    listener!!.onFetchFailure("Something went wrong. Please try again later")
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return parameters
            }

        }
        Log.i("Make", stringRequest.toString())
        // Volley request policy, only one time request to avoid duplicate transaction
        stringRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        //adding request to queue
        RequestQueueService.getInstance(context).addToRequestQueue(stringRequest)
    }
}



