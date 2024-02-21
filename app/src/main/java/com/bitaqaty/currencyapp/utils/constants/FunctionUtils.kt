package com.bitaqaty.currencyapp.utils.constants

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object FunctionUtils {

    private const val TAG = "FunctionUtils"

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.packageManager
                    .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }


    fun isConnected(context: Context): Boolean {
        var status = false
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                // connected to the internet
                status = true
            }
        } else {
            if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting) {
                // connected to the internet
                status = true
            }
        }
        return status
    }

    fun <T> toList(`object`: List<Any?>?, desiredClass: Class<T>?): List<T> {
        val transformedList: MutableList<T> = ArrayList()
        if (`object` != null) {
            for (result in `object`) {
                val json = Gson().toJson(result)
                val model = Gson().fromJson(json, desiredClass)
                transformedList.add(model)
            }
        }
        return transformedList
    }

    fun timeThirdsHasPassed(passedTime: Double, fullTime: Double): Boolean {
        return (passedTime / fullTime > (1.0 / 3.0))
    }
}