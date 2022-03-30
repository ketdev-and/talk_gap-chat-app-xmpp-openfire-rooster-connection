package com.blikoon.roosterplus.presistence;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;


import com.blikoon.roosterplus.model.Chat;

/**
 * Created by Everest on 2021/3/17
 */

public class ChatCursorWrapper extends CursorWrapper {
    private  static  final  String LOGTAG = "ChatCursorWrapper";
    /**
     *  Create a cursor Wrapper
     *
     * @param cursor The underlying cursor to wrap.
     */

    public ChatCursorWrapper (Cursor cursor){
        super(cursor);

    }

    public Chat getChat (){
        String jid = getString(getColumnIndex(Chat.Cols.CONTACT_JID));
        String contactType = getString(getColumnIndex(Chat.Cols.CONTACT_TYPE));
        String lastMessage = getString(getColumnIndex(Chat.Cols.LAST_MESSAGE));
        long unreadCount = getLong(getColumnIndex(Chat.Cols.UNREAD_COUNT));
        long lastMessageTimeStamp = getLong(getColumnIndex(Chat.Cols.LAST_MESSAGE_TIME_STAMP));
        int uniqueId = getInt(getColumnIndex(Chat.Cols.CHAT_UNIQUE_ID));

        Log.d(LOGTAG, "Got a Chat from database the Unique ID is :" + uniqueId);


        Chat.ContactType chatType = null;

         if(contactType.equals("Group")){
             chatType = Chat.ContactType.GROUP;
         }   else  if (contactType.equals("ONE_ON_ONE")){
             chatType = Chat.ContactType.ONE_ON_ONE;
         }
         Chat chat = new Chat(jid, lastMessage, chatType, lastMessageTimeStamp, unreadCount);
         chat.setPersistID(uniqueId);
         return chat;

    }

}
