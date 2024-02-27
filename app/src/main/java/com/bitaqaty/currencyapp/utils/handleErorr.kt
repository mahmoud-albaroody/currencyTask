package com.bitaqaty.currencyapp.utils

import okhttp3.ResponseBody
import org.json.JSONObject

fun handleResponseError(response: ResponseBody?): String {
    var error = ""
    error = try {
        val jObjError = JSONObject(response?.string())
        jObjError.getString("message")
    } catch (e: java.lang.Exception) {
        e.message.toString()
    }
    return error
}