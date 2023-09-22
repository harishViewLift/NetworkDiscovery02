package com.example.networkdiscovery02;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NSDServer {
    public static final String TAG = "NSDServerWhy";
    private NsdManager mNsdManager;
    private NsdManager.RegistrationListener mRegistrationListener;
    private IRegisterState registerState; //NSD service interface object
    private String mServerName;
    private final String mServerType = "_http._tcp."; // Server type, the client needs to scan the server for consistency

    public NSDServer() {
    }

    public void startNSDServer(Context context, String serviceName, int port) {
        initializeRegistrationListener();
        registerService(context, serviceName, port);
    }

    //Initialize the registered listener
    private void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "NsdServiceInfo onRegistrationFailed");
                if (registerState != null) {
                    registerState.onRegistrationFailed(serviceInfo, errorCode);
                }
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.i(TAG, "onUnregistrationFailed serviceInfo: " + serviceInfo + " ,errorCode:" + errorCode);
                if (registerState != null) {
                    registerState.onUnRegistrationFailed(serviceInfo, errorCode);
                }
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                mServerName = serviceInfo.getServiceName();
                Log.i(TAG, "onServiceRegistered: " + serviceInfo.toString());
                Log.i(TAG, "mServerName onServiceRegistered: " + mServerName);
                if (registerState != null) {
                    registerState.onServiceRegistered(serviceInfo);
                }
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "onServiceUnregistered serviceInfo: " + serviceInfo);
                if (registerState != null) {
                    registerState.onServiceUnregistered(serviceInfo);
                }
            }
        };
    }

    private void registerService(Context context, String serviceName, int port) {
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setPort(port);
        serviceInfo.setServiceType(mServerType);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void stopNSDServer() {
        mNsdManager.unregisterService(mRegistrationListener);
    }


    //NSD service registration listening interface
    public interface IRegisterState {
        void onServiceRegistered(NsdServiceInfo serviceInfo); //Registration of NSD successful

        void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode); //NSD registration failed

        void onServiceUnregistered(NsdServiceInfo serviceInfo); //Cancel NSD registration successfully

        void onUnRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode); //Cancel NSD registration failed

    }


    //Set the NSD service interface object
    public void setRegisterState(IRegisterState registerState) {
        this.registerState = registerState;
    }
}
