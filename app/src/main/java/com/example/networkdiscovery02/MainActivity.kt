package com.example.networkdiscovery02

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var clientMessage: TextView? = null
    var serverTitle: TextView? = null
    var newNsdServerView: EditText? = null
    var nsdServerManager: NsdServerManager? = null

    /**
     * Register the name and port of the NSD service. This can set the default fixed address, which is used by the client to obtain the server address and port through NSD_SERVER_NAME filtering.
     */
    private var nsd_server_name = "WhySystem"
    private val nsd_server_port = 8088
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        nsdServerManager = NsdServerManager.getInstance(this)
        nsdServerManager!!.registerNsdServer(nsd_server_name)
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
    fun resetServerName(view: View?) {
        nsd_server_name = newNsdServerView!!.text.toString()
        serverTitle!!.text = "Nsd server----$nsd_server_name"
        nsdServerManager!!.registerNsdServer(nsd_server_name)
    }

    fun unRegister(view: View?) {
        nsdServerManager!!.unRegisterNsdServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        nsdServerManager!!.unRegisterNsdServer()
    }

    companion object {
        private const val TAG = "ServerWhy"
    }
}