package com.example.networkdiscovery02;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;


/**
 * @author why
 * @date 2019.04.28
 */
public class NsdClient {

    public static final String TAG = "NsdClientWhy";

    private final String NSD_SERVER_TYPE = "_http._tcp.";
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolverListener;
    public NsdManager mNsdManager;
    private Context mContext;
    private String mServiceName;
    private Handler mHandler;
    private IServerFound mIServerFound;
    private ArrayList<String> discoveryList=new ArrayList<>();
    private ArrayList<String> resolveList=new ArrayList<>();

    /**
     * @param context: context object
     * @param serviceName client scans the specified address
     * @param iServerFound callback
     */
    public NsdClient(Context context, String serviceName, IServerFound iServerFound) {
        mContext = context;
        mServiceName = serviceName;
        mIServerFound = iServerFound;
    }

    public void startNSDClient(final Handler handler) {

        new Thread(){
            @Override
            public void run() {
                mHandler=handler;
                mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
                initializeDiscoveryListener();//Initialize the listener
                initializeResolveListener();//Initialize the parser
                mNsdManager.discoverServices(NSD_SERVER_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);//Start scanning
            }
        }.start();
    }

    /**
     * Scan NsdServiceInfo before parsing
     */
    private void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.e(TAG, "onStartDiscoveryFailed():");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.e(TAG, "onStopDiscoveryFailed():");
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.e(TAG, "onDiscoveryStarted():");

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.e(TAG, "onDiscoveryStopped():");
            }

            /**
             *
             * @param serviceInfo
             */
            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "onServiceFound: "+serviceInfo );
                discoveryList.add(serviceInfo.toString());
                //According to the defined name of our server, specify the NsdServiceInfo to be parsed
                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    mNsdManager.resolveService(serviceInfo, mResolverListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "onServiceLost(): serviceInfo=" + serviceInfo);
                discoveryList.remove(serviceInfo.toString());
            }
        };
    }

    /**
     * Parse the NsdServiceInfo found
     */
    private void initializeResolveListener() {
        mResolverListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                int port = serviceInfo.getPort();
                String serviceName = serviceInfo.getServiceName();
                String hostAddress = serviceInfo.getHost().getHostAddress();

                Message message=Message.obtain();
                message.what=1;
                message.obj=serviceInfo.toString();
                mHandler.sendMessage(message);

                Log.e(TAG, "onServiceResolved Resolved:" + " host:" + hostAddress + ":" + port + " ----- serviceName: " + serviceName);
                resolveList.add(" host:" + hostAddress + ":" + port );
                //TODO establish network connection

            }
        };
    }

    public void stopNSDServer() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public interface IServerFound {

        /**
         * Callback specifies the parsing result
         */
        void onServerFound(NsdServiceInfo serviceInfo, int port);

        /**
         * No suitable callback failed
         */
        void onServerFail();
    }
}
