package com.blikoon.roosterplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blikoon.roosterplus.adapters.ChatListAdapter;
import com.blikoon.roosterplus.adapters.ChatMessagesAdapter;
import com.blikoon.roosterplus.model.ChatMessage;
import com.blikoon.roosterplus.model.ChatMessagesModel;
import com.blikoon.roosterplus.ui.keyboardUtil;
import com.blikoon.roosterplus.xmpp.TalkGapChatConnectionService;

public class ChatViewActivity extends AppCompatActivity implements ChatMessagesAdapter.OnInformRecyclerViewToScrollDownListener, keyboardUtil.KeyboardVisibilityListener{
    RecyclerView chatMessagesRecyclerView;
    private EditText textSendEditText;
    private ImageButton sendMessageButton;
    ChatMessagesAdapter adapter;
    private String counterpartJid;
    private BroadcastReceiver mReceiveMessageBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        //GET THE counter Jid, username, chatbackground, fullname and online status.

        Intent intent = getIntent();
        counterpartJid = intent.getStringExtra("contact_jid");
        setTitle(counterpartJid);


        chatMessagesRecyclerView = (RecyclerView) findViewById(R.id.chatMessagesRecyclerView);
        chatMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter = new ChatMessagesAdapter(getApplicationContext(), "user1@server.com" );
        adapter.setmOnInformRecyclerViewToScrollDownListener(this);
        chatMessagesRecyclerView.setAdapter(adapter);

        textSendEditText = (EditText) findViewById(R.id.textinput);
        sendMessageButton = (ImageButton) findViewById(R.id.textSendButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             // THE CODE TO SEND THE MESSAGE ( WHEN CLICK ON SEND BUTTON )
                TalkGapChatConnectionService.getConnection().sendMessages(textSendEditText.getText().toString(), counterpartJid);
                adapter.onMessageAdd();
                textSendEditText.getText().clear();

            }
        });
        keyboardUtil.setKeyboardVisibilityListener(this,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.activity_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.received_message)
        {
            ChatMessagesModel.get(getApplicationContext()).addMessage(new ChatMessage("This is a message you recevied from string", System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "User@server.com"));

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiveMessageBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // to scroll down once we open the chat view activity
        adapter.informRecyclerViewToScrollDown();

        mReceiveMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action)
                {
                    case Constants.BroadCastMessage.UI_NEW_MASSAGE_FLAG:
                        adapter.onMessageAdd();
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter( Constants.BroadCastMessage.UI_NEW_MASSAGE_FLAG);
        registerReceiver(mReceiveMessageBroadcastReceiver, filter);



    }

    @Override
    public void onInformRecyclerViewToScrollDown(int size) {
      chatMessagesRecyclerView.scrollToPosition(size-1);

    }

    @Override
    public void onKeyboardVisibilityChanged(boolean keyboardVisible) {
        adapter.informRecyclerViewToScrollDown();

    }
}