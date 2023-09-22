package com.example.networkdiscovery02

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log

class NSDServer {
    private var mNsdManager: NsdManager? = null
    private var mRegistrationListener: RegistrationListener? = null
    private var registerState: IRegisterState? = null //NSD service interface object
    private var mServerName: String? = null
    private val mServerType =
        "_http._tcp." // Server type, the client needs to scan the server for consistency

    fun startNSDServer(context: Context, serviceName: String, port: Int) {
        initializeRegistrationListener()
        registerService(context, serviceName, port)
    }

    //Initialize the registered listener
    private fun initializeRegistrationListener() {
        mRegistrationListener = object : RegistrationListener {
            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "NsdServiceInfo onRegistrationFailed")
                if (registerState != null) {
                    registerState!!.onRegistrationFailed(serviceInfo, errorCode)
                }
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.i(TAG, "onUnregistrationFailed serviceInfo: $serviceInfo ,errorCode:$errorCode")
                if (registerState != null) {
                    registerState!!.onUnRegistrationFailed(serviceInfo, errorCode)
                }
            }

            override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
                mServerName = serviceInfo.serviceName
                Log.i(TAG, "onServiceRegistered: $serviceInfo")
                Log.i(TAG, "mServerName onServiceRegistered: $mServerName")
                if (registerState != null) {
                    registerState!!.onServiceRegistered(serviceInfo)
                }
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                Log.i(TAG, "onServiceUnregistered serviceInfo: $serviceInfo")
                if (registerState != null) {
                    registerState!!.onServiceUnregistered(serviceInfo)
                }
            }
        }
    }

    private fun registerService(context: Context, serviceName: String, port: Int) {
        mNsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
        val serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = serviceName
        serviceInfo.port = port
        serviceInfo.serviceType = mServerType
        mNsdManager!!.registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            mRegistrationListener
        )
    }

    fun stopNSDServer() {
        mNsdManager!!.unregisterService(mRegistrationListener)
    }

    //NSD service registration listening interface
    interface IRegisterState {
        fun onServiceRegistered(serviceInfo: NsdServiceInfo?) //Registration of NSD successful
        fun onRegistrationFailed(
            serviceInfo: NsdServiceInfo?,
            errorCode: Int
        ) //NSD registration failed

        fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) //Cancel NSD registration successfully
        fun onUnRegistrationFailed(
            serviceInfo: NsdServiceInfo?,
            errorCode: Int
        ) //Cancel NSD registration failed
    }

    //Set the NSD service interface object
    fun setRegisterState(registerState: IRegisterState?) {
        this.registerState = registerState
    }

    companion object {
        const val TAG = "NSDServerWhy"
    }
}