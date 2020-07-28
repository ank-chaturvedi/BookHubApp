package com.basics.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConectionManager {

    fun InternetConnection(context:Context):Boolean{

        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork:NetworkInfo? = connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected !=null){
            return activeNetwork.isConnected
        }  else{
            return false
        }

    }
}