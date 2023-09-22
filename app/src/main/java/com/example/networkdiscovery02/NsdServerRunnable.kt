package com.example.networkdiscovery02

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.example.networkdiscovery02.NSDServer.IRegisterState

class NsdServerRunnable(
    /**
     * Register the name and port of the NSD service. This can set the default fixed address, which is used by the client to obtain the server address and port through NSD_SERVER_NAME filtering.
     */
    private val nsdServerName: String,
    private val nsdServer: NSDServer,
    private val context: Context
) : Runnable {
    private val NSD_PORT = 8088
    override fun run() {
        nsdServer.startNSDServer(context, nsdServerName, NSD_PORT)
        nsdServer.setRegisterState(object : IRegisterState {
            override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
                Log.e(TAG, "onServiceRegistered: $serviceInfo")
                //Already registered to stop the service
                // nsdServer.stopNSDServer();
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}
            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {}
            override fun onUnRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}
        })
    }

    companion object {
        private const val TAG = "NsdServerRunnableWhy"
    }
}