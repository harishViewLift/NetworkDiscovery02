package com.example.networkdiscovery02;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * author:why
 * created on: 2019/4/28 15:17
 * description:
 */
public class NsdClientManager {

    private static final String TAG = "NsdClientManagerWhy";
    /**
     * Nsd Client search
     */
    private NsdClient nsdClient;
    private Context context;
    private Handler mHandler;
    private volatile static NsdClientManager mNsdClientManager=null;

    private NsdClientManager(Context context,Handler handler){
        this.context=context;
        this.mHandler=handler;
    }

    /**
     * DCL Single Instance
     * @param context
     * @param handler
     * @return
     */
    public static NsdClientManager getInstance(Context context,Handler handler){
        if(mNsdClientManager==null){
            synchronized (NsdClientManager.class){
                if(mNsdClientManager==null){
                    mNsdClientManager=new NsdClientManager(context,handler);
                }
            }
        }
        return mNsdClientManager;
    }

    /**
     * Search the registered server related parameters through Nsd for Socket connection (IP and Port)
     */
    public void searchNsdServer(final String nsdServerName) {
        nsdClient = new NsdClient(context, nsdServerName, new NsdClient.IServerFound() {
            @Override
            public void onServerFound(NsdServiceInfo info, int port) {
                if (info != null) {
                    Log.e(TAG, "onServerFound: "+info.toString() );
                    if (info.getServiceName().equals(nsdServerName)) {
                        // Scan to the specified server and stop scanning.
                        nsdClient.stopNSDServer();
                    }
                }
            }

            @Override
            public void onServerFail() {

            }
        });

        nsdClient.startNSDClient(mHandler);
    }
}