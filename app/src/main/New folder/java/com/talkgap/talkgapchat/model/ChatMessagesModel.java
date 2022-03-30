package com.blikoon.roosterplus.model;

import android.content.Context;
import android.location.OnNmeaMessageListener;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesModel {



    private static ChatMessagesModel sChatMessagesModel;
    private Context context;
    List<ChatMessage> messages;




    public static ChatMessagesModel get(Context context)
    {
        if (sChatMessagesModel == null)
        {
            sChatMessagesModel = new ChatMessagesModel(context);
        }

        return   sChatMessagesModel;
    }

    private  ChatMessagesModel (Context context)
    {
       this.context = context;
       ChatMessage  message1 = new ChatMessage("Hey there", System.currentTimeMillis(), ChatMessage.Type.SENT, "user@server.com");

       ChatMessage  message2 = new ChatMessage("Hey are you doing?", System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "user2@server.com");

       messages = new ArrayList<>();
       messages.add(message1);
       messages.add(message2);
       messages.add(message1);
       messages.add(message2);
       messages.add(message1);
       messages.add(message2);



    }

    public List<ChatMessage> getMessages()
    {
        return messages;
    }

    public  void  addMessage (ChatMessage message)
    {
        messages.add(message);

    }
}
