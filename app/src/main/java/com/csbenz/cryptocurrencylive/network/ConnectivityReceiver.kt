package com.csbenz.cryptocurrencylive.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {
        orderBookConnectivityReceiverListener?.onNetworkConnectionChanged(NetworkUtils.isNetworkAvailable(context))
        tradesConnectivityReceiverListener?.onNetworkConnectionChanged(NetworkUtils.isNetworkAvailable(context))
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var orderBookConnectivityReceiverListener: ConnectivityReceiverListener? = null
        var tradesConnectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}