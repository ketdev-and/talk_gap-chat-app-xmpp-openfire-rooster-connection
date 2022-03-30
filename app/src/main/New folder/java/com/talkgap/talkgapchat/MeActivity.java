package com.blikoon.roosterplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.blikoon.roosterplus.xmpp.TalkGapChatConnection;
import com.blikoon.roosterplus.xmpp.TalkGapChatConnectionService;

public class MeActivity extends AppCompatActivity {

    private BroadcastReceiver mBroadcastReceiver;

    private TextView connectionStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        String status;
        TalkGapChatConnection  connection = TalkGapChatConnectionService.getConnection();
        connectionStatusTextView = (TextView) findViewById(R.id.connection_status);

        if (connection != null)
        {
            status = connection.getConnectionStateString();
            connectionStatusTextView.setText(status);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
         mBroadcastReceiver = new BroadcastReceiver() {
             @Override
             public void onReceive(Context context, Intent intent) {
                 String action = intent.getAction();

                 switch (action)
                 {
                     case Constants.BroadCastMessage.UI_CONNECTION_STATUS_CHANGE_FLAG:
                         String status = intent.getStringExtra(Constants.UI_CONNECTION_STATUS_CHANGE);
                         connectionStatusTextView.setText(status);
                         break;

                 }
             }
         };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BroadCastMessage.UI_CONNECTION_STATUS_CHANGE_FLAG);
        this.registerReceiver(mBroadcastReceiver, filter);


    }
}