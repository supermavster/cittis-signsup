package com.cittis.signsup.connection


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import android.view.Window
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cittis.signsup.R
import java.util.*

class RequestQueueService private constructor(context: Context) {
    private var mRequestQueue: RequestQueue? = null

    // getApplicationContext() is key, it keeps you from leaking the
    // Activity or BroadcastReceiver if someone passes one in.
    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.applicationContext)
            }
            return mRequestQueue
        }

    val requestHeader: Map<String, String>
        get() = HashMap()

    init {
        mCtx = context
        mRequestQueue = requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>?) {
        req!!.retryPolicy = DefaultRetryPolicy(
            5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue?.add(req)
    }

    fun clearCache() {
        mRequestQueue!!.cache.clear()
    }

    fun removeCache(key: String) {
        mRequestQueue!!.cache.remove(key)
    }

    companion object {
        private var mInstance: RequestQueueService? = null
        private lateinit var mCtx: Context
        private var mProgressDialog: Dialog? = null

        @Synchronized
        fun getInstance(context: Context): RequestQueueService {
            if (mInstance == null) {
                mInstance = RequestQueueService(context)
            }
            return mInstance as RequestQueueService
        }

        //To show alert / error message
        fun showAlert(message: String, context: Context) {
            try {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Error!")
                builder.setMessage(message)
                builder.setPositiveButton("OK", null)

                builder.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        //Start showing progress
        fun showProgressDialog(fragment: Fragment, context: Context) {
            fragment.activity!!.runOnUiThread {
                if (mProgressDialog != null) {
                    if (mProgressDialog!!.isShowing == true) cancelProgressDialog()
                }

                mProgressDialog = Dialog(context)
                mProgressDialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
                mProgressDialog!!.setContentView(R.layout.progress_indicator)
                mProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mProgressDialog!!.show()
                mProgressDialog!!.setCancelable(false)
            }

        }

        //Stop showing progress
        fun cancelProgressDialog() {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }
            }
        }
    }

}
