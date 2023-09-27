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

    val SERVICES_DOMAIN = "_services."
//    val SERVICES_DOMAIN = "_services._dns-sd._udp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client: NsdClientManager? = NsdClientManager.getInstance(this)
        client?.searchNsdServer(SERVICES_DOMAIN)
    }
}