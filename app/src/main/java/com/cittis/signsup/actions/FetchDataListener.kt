package com.cittis.signsup.actions

import org.json.JSONObject

interface FetchDataListener {
    fun onFetchComplete(data: JSONObject)

    fun onFetchFailure(msg: String)

    fun onFetchStart()
}
