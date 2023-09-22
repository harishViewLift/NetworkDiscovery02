package com.example.networkdiscovery02

import android.content.Context
import android.util.Log

class NsdServerManager(private val context: Context) {
    /**
     * Register for Nsd service
     * @param nsdServerName
     */
    fun registerNsdServer(nsdServerName: String?) {
        Thread(NsdServerRunnable(nsdServerName!!, nsdServer!!, context)).start()
    }

    /**
     * Unregister NsdServer
     */
    fun unRegisterNsdServer() {
        Log.e(TAG, "unRegisterNsdServer: ")
        nsdServer!!.stopNSDServer()
    }

    companion object {
        private const val TAG = "NsdServerManagerWhy"

        @Volatile
        private var nsdServerManager: NsdServerManager? = null
        private var nsdServer: NSDServer? = null
        fun getInstance(context: Context): NsdServerManager? {
            if (nsdServerManager == null) {
                synchronized(NsdServerManager::class.java) {
                    if (nsdServerManager == null) {
                        nsdServer = NSDServer()
                        nsdServerManager = NsdServerManager(context)
                    }
                }
            }
            return nsdServerManager
        }
    }
}