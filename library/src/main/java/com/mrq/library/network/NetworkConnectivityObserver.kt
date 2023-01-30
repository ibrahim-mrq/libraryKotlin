package com.mrq.library.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(context: Context) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var callback: ConnectivityManager.NetworkCallback? = null

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(callback!!)
            }
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback!!)
            }
        }.distinctUntilChanged()
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(callback!!)
    }

}

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}