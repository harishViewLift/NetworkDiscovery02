package com.example.networkdiscovery02;

import android.content.Context;
import android.util.Log;

public class NsdServerManager {
    private static final String TAG = "NsdServerManagerWhy";
    private volatile static NsdServerManager nsdServerManager;
    private static NSDServer nsdServer;
    private Context context;

    public NsdServerManager(Context context){
        this.context=context;
    }

    public static NsdServerManager getInstance(Context context){
        if(nsdServerManager==null){
            synchronized (NsdServerManager.class){
                if (nsdServerManager==null) {
                    nsdServer=new NSDServer();
                    nsdServerManager = new NsdServerManager(context);
                }
            }
        }
        return nsdServerManager;
    }

    /**
     * Register for Nsd service
     * @param nsdServerName
     */
    public void registerNsdServer(String nsdServerName) {
        new Thread(new NsdServerRunnable(nsdServerName,nsdServer,context)).start();
    }

    /**
     * Unregister NsdServer
     */
    public void unRegisterNsdServer(){
        Log.e(TAG, "unRegisterNsdServer: " );
        nsdServer.stopNSDServer();
    }


}
