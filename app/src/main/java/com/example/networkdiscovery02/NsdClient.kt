package com.example.networkdiscovery02

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Message
import android.util.Log

/**
 * @param context: context object
 * @param serviceName client scans the specified address
 * @param iServerFound callback
 */
class NsdClient(
    private val mContext: Context,
    private val mServiceName: String,
    private val mIServerFound: IServerFound
) {

    private val NSD_SERVER_TYPE = "_http._tcp."
    private var mDiscoveryListener: DiscoveryListener? = null
    private var mResolverListener: NsdManager.ResolveListener? = null
    var mNsdManager: NsdManager? = null
    private var mHandler: Handler? = null
    private val discoveryList = ArrayList<String>()
    private val resolveList = ArrayList<String>()

    fun startNSDClient(handler: Handler?) {
        object : Thread() {
            override fun run() {
                mHandler = handler
                mNsdManager = mContext.getSystemService(Context.NSD_SERVICE) as NsdManager
                initializeDiscoveryListener() //Initialize the listener
                initializeResolveListener() //Initialize the parser
                mNsdManager!!.discoverServices(
                    NSD_SERVER_TYPE,
                    NsdManager.PROTOCOL_DNS_SD,
                    mDiscoveryListener
                ) //Start scanning
            }
        }.start()
    }

    /**
     * Scan NsdServiceInfo before parsing
     */
    private fun initializeDiscoveryListener() {
        mDiscoveryListener = object : DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                mNsdManager!!.stopServiceDiscovery(this)
                Log.e(TAG, "onStartDiscoveryFailed():")
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                mNsdManager!!.stopServiceDiscovery(this)
                Log.e(TAG, "onStopDiscoveryFailed():")
            }

            override fun onDiscoveryStarted(serviceType: String) {
                Log.e(TAG, "onDiscoveryStarted():")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.e(TAG, "onDiscoveryStopped():")
            }

            /**
             *
             * @param serviceInfo
             */
            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.e(TAG, "onServiceFound: $serviceInfo")
                discoveryList.add(serviceInfo.toString())
                //According to the defined name of our server, specify the NsdServiceInfo to be parsed
                if (serviceInfo.serviceName == mServiceName) {
                    mNsdManager!!.resolveService(serviceInfo, mResolverListener)
                }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                Log.e(TAG, "onServiceLost(): serviceInfo=$serviceInfo")
                discoveryList.remove(serviceInfo.toString())
            }
        }
    }

    /**
     * Parse the NsdServiceInfo found
     */
    private fun initializeResolveListener() {
        mResolverListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}
            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                val port = serviceInfo.port
                val serviceName = serviceInfo.serviceName
                val hostAddress = serviceInfo.host.hostAddress
                val message = Message.obtain()
                message.what = 1
                message.obj = serviceInfo.toString()
                mHandler!!.sendMessage(message)
                Log.e(
                    TAG,
                    "onServiceResolved Resolved: host:$hostAddress:$port ----- serviceName: $serviceName"
                )
                resolveList.add(" host:$hostAddress:$port")
                //TODO establish network connection
            }
        }
    }

    fun stopNSDServer() {
        mNsdManager!!.stopServiceDiscovery(mDiscoveryListener)
    }

    interface IServerFound {
        /**
         * Callback specifies the parsing result
         */
        fun onServerFound(serviceInfo: NsdServiceInfo?, port: Int)

        /**
         * No suitable callback failed
         */
        fun onServerFail()
    }

    companion object {
        const val TAG = "NsdClientWhy"
    }
}