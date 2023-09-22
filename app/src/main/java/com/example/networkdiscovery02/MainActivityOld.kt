package com.example.networkdiscovery02

//class MainActivity : AppCompatActivity() {
//    private var nsdManager: NsdManager? = getSystemService(Context.NSD_SERVICE) as NsdManager?
//    val discoveryList: ArrayList<String> = arrayListOf()
//    val resolveList: ArrayList<String> = arrayListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//
//    fun initializeDiscoveryListener(){
//       val mDiscoveryListener = object : NsdManager.DiscoveryListener {
//           override fun onStartDiscoveryFailed(servieType: String?, errorCode: Int) {
//                nsdManager?.stopServiceDiscovery(this)
//           }
//
//           override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
//               nsdManager?.stopServiceDiscovery(this)
//           }
//
//           override fun onDiscoveryStarted(serviceType: String?) {
//               Log.d("NsdManager", "onDiscoveryStarted")
//           }
//
//           override fun onDiscoveryStopped(serviceType: String?) {
//               Log.d("NsdManager", "onDiscoveryStopped")
//           }
//
//           override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
//               discoveryList.add(serviceInfo.toString())
//               if (serviceInfo.serviceName == mServiceName){
//                   nsdManager.resolveService(serviceInfo, mRes)
//               }
//           }
//
//           override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
//               discoveryList.remove(serviceInfo.toString())
//           }
//
//       }
//    }
//
//    private val resolveListener: NsdManager.ResolveListener = object : NsdManager.ResolveListener {
//
//        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
//            // Called when the resolve fails. Use the error code to debug.
//            Log.e("NsdManager", "Resolve failed: $errorCode")
//        }
//
//        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
//            Log.e("NsdManager", "Resolve Succeeded. $serviceInfo")
//            val port = serviceInfo.port
//            val serviceName = serviceInfo.serviceName
//            val hostAddress = serviceInfo.host.hostAddress
//
//            resolveList.add("Host: $hostAddress, port: $port")
//
////            if (serviceInfo.serviceName == mServiceName) {
////                Log.d("NsdManager", "Same IP.")
////                return
////            }
////            mService = serviceInfo
////            val port: Int = serviceInfo.port
////            val host: InetAddress = serviceInfo.host
//        }
//    }
//
//    fun startNSDClient(handler: Handler){
//
//    }
//
//}