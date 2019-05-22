package com.cittis.signsup.connection

import android.content.Context
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.cittis.signsup.actions.FetchDataListener
import org.json.JSONException
import org.json.JSONObject


class GETAPIRequest {
    @Throws(JSONException::class)
    fun request(context: Context, listener: FetchDataListener?, ApiURL: String) {
        listener?.onFetchStart()
        //base server URL
        val postRequest = JsonObjectRequest(
            Request.Method.GET, ApiURL, null,
            Response.Listener { response ->
                try {
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
            }, Response.ErrorListener { error ->
                if (error is NoConnectionError) {
                    listener!!.onFetchFailure("Network Connectivity Problem")
                } else if (error.networkResponse != null && error.networkResponse.data != null) {
                    val volley_error = VolleyError(String(error.networkResponse.data))
                    var errorMessage = ""
                    try {
                        val errorJson = JSONObject(volley_error.message.toString())
                        if (errorJson.has("error")) errorMessage = errorJson.getString("error")
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
            })

        RequestQueueService.getInstance(context)
            .addToRequestQueue(postRequest.setShouldCache(false))
        //.addToRequestQueue(postRequest.setShouldCache(false))
    }
}



