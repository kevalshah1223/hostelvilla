package com.chausat.hotelvilla

import android.content.Context
import android.net.ConnectivityManager


class MainClass {
    fun isConnected(context: Context): Boolean? {
        var flag = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                flag = true
            }
        } else {
            flag = false
        }
        return flag
    }

    fun getURL() : String{
            return "http://10.0.2.2/HostelVilla/"
    }
}