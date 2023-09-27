package com.example.networkdiscovery02

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.util.Log
import com.example.networkdiscovery02.NsdClient.IServerFound

class NsdClientManager private constructor(
    private val context: Context,
) {
    /**
     * Nsd Client search
     */
    private var nsdClient: NsdClient? = null

    /**
     * Search the registered server related parameters through Nsd for Socket connection (IP and Port)
     */
    fun searchNsdServer(nsdServerName: String) {
        nsdClient = NsdClient(context, nsdServerName, object : IServerFound {
            override fun onServerFound(info: NsdServiceInfo?, port: Int) {
                if (info != null) {
                    Log.e(TAG, "onServerFound: $info")
                    if (info.serviceName == nsdServerName) {
                        // Scan to the specified server and stop scanning.
                        nsdClient!!.stopNSDServer()
                    }
                }
            }

            override fun onServerFail() {}
        })
        nsdClient!!.startNSDClient()
    }

    companion object {
        private const val TAG = "NsdClientManagerWhy"

        @Volatile
        private var mNsdClientManager: NsdClientManager? = null

        /**
         * DCL Single Instance
         * @param context
         * @param handler
         * @return
         */
        fun getInstance(context: Context): NsdClientManager? {
            if (mNsdClientManager == null) {
                synchronized(NsdClientManager::class.java) {
                    if (mNsdClientManager == null) {
                        mNsdClientManager = NsdClientManager(context)
                    }
                }
            }
            return mNsdClientManager
        }
    }
}