package com.example.networkdiscovery02;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ServerWhy";

    TextView clientMessage;
    TextView serverTitle;
    EditText newNsdServerView;
    NsdServerManager nsdServerManager;

    /**
     * Register the name and port of the NSD service. This can set the default fixed address, which is used by the client to obtain the server address and port through NSD_SERVER_NAME filtering.
     */
    private String nsd_server_name = "WhySystem";
    private int nsd_server_port = 8088;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        nsdServerManager = NsdServerManager.getInstance(this);
        nsdServerManager.registerNsdServer(nsd_server_name);
    }


    private void initUI() {
        clientMessage = findViewById(R.id.message_from_client);
        serverTitle = findViewById(R.id.server_title);
        serverTitle.append("----" + nsd_server_name);
        newNsdServerView = findViewById(R.id.nsd_server_name);
    }


    /**
     * Reset NSD server name
     *
     * @param view
     */
    public void resetServerName(View view) {
        nsd_server_name = newNsdServerView.getText().toString();
        serverTitle.setText("Nsd server----" + nsd_server_name);
        nsdServerManager.registerNsdServer(nsd_server_name);
    }

    public void unRegister(View view) {
        nsdServerManager.unRegisterNsdServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nsdServerManager.unRegisterNsdServer();
    }
}
