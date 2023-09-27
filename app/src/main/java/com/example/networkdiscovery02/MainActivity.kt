package com.example.networkdiscovery02

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    var clientMessage: TextView? = null
    var serverTitle: TextView? = null
    var newNsdServerView: EditText? = null
    /**
     * Register the name and port of the NSD service. This can set the default fixed address, which is used by the client to obtain the server address and port through NSD_SERVER_NAME filtering.
     */
    private var nsd_server_name = "WhySystem"
    private val nsd_server_port = 8088
    val LOCAL_DOMAIN = "local."
    val SERVICES_DOMAIN = "_services."
//    val SERVICES_DOMAIN = "_services._dns-sd._udp"

    private val SERVICE_TYPE = "_mynsd._tcp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()

        val handler = Handler()

        val client: NsdClientManager? = NsdClientManager.getInstance(this, handler)
        client?.searchNsdServer(SERVICES_DOMAIN)
//        client?.searchNsdServer("_airplay._tcp.")
    }

    fun scanSubNet(subnet: String): ArrayList<String>? {
        val hosts = ArrayList<String>()
        var inetAddress: InetAddress? = null
        for (i in 1..9) {
            Log.d(NsdClient.TAG, "Trying: $subnet$i")
            try {
                inetAddress = InetAddress.getByName(subnet + i.toString())
                if (inetAddress.isReachable(1000)) {
                    hosts.add(inetAddress.hostName)
                    Log.d(NsdClient.TAG, inetAddress.hostName)
                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return hosts
    }

    private fun initUI() {
        clientMessage = findViewById(R.id.message_from_client)
        serverTitle = findViewById(R.id.server_title)
        serverTitle!!.text = "----$nsd_server_name"
        newNsdServerView = findViewById(R.id.nsd_server_name)
    }

    /**
     * Reset NSD server name
     *
     * @param view
     */
    companion object {
        private const val TAG = "ServerWhy"
    }
}