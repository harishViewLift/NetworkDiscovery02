package com.example.networkdiscovery02;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NsdServerRunnable implements Runnable {

    private static final String TAG = "NsdServerRunnableWhy";
    /**
     * Register the name and port of the NSD service. This can set the default fixed address, which is used by the client to obtain the server address and port through NSD_SERVER_NAME filtering.
     */
    private String nsdServerName;
    private final int NSD_PORT = 8088;
    private NSDServer nsdServer;
    private Context context;

    public NsdServerRunnable(String nsdServerName, NSDServer nsdServer, Context context){
        this.nsdServerName=nsdServerName;
        this.nsdServer=nsdServer;
        this.context=context;
    }

    @Override
    public void run() {

        nsdServer.startNSDServer(context, nsdServerName, NSD_PORT);
        nsdServer.setRegisterState(new NSDServer.IRegisterState() {
            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "onServiceRegistered: " + serviceInfo.toString());
                //Already registered to stop the service
                // nsdServer.stopNSDServer();
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }
            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
            @Override
            public void onUnRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }
        });
    }
}
