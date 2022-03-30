package com.blikoon.roosterplus.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blikoon.roosterplus.presistence.ChatCursorWrapper;
import com.blikoon.roosterplus.presistence.DatabaseBackend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Everest  March /8/ 2021
 */

public class ChatModel {

    private static final String LOGTAG = "ChatModel";
    private static ChatModel sChatsModel;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ChatModel get(Context context)
    {
        if(sChatsModel == null)
        {
            sChatsModel = new ChatModel(context);
        }

        return sChatsModel;
    }

    private ChatModel(Context context)
    {
        mContext = context;
        mDatabase = DatabaseBackend.getInstance(mContext).getWritableDatabase();
    }

    public List<Chat> getChats()
    {
        List<Chat> chats = new ArrayList<>();
        ChatCursorWrapper cursor = queryChats(null, null);
     try
     {
         cursor.moveToFirst();
         while (!cursor.isAfterLast())
         {
             Log.d(LOGTAG, "Got a chat from db: Timestap :" +cursor.getChat().getLastMessage());
             chats.add(cursor.getChat());
             cursor.moveToNext();
         }
     } finally {
         cursor.close();
     }
     return chats;

    }

    public boolean addChat (Chat c)
    {
        // TODO: Check if chat already in the before adding
        ContentValues values = c.getContentValues();
        if ((mDatabase.insert(Chat.TABLE_NAME, null, values)==1))
        {
            return  false;
        } else
        {
            return true;
        }
    }

    public boolean deleteChat(Chat c)
    {
        return deleteChat(c.getPersistID());
    }

   public boolean deleteChat(int uniqueId)
   {
       int value = mDatabase.delete(Chat.TABLE_NAME, Chat.Cols.CHAT_UNIQUE_ID+"=?", new String[] {String.valueOf(uniqueId)});
      if (value == 1)
      {
          Log.d(LOGTAG, "Successufull deleted a record");
          return true;
      } else
      {
          Log.d(LOGTAG, "Counld not delete record");
          return  false;
      }

   }



    private ChatCursorWrapper queryChats(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                Chat.TABLE_NAME,
                null, //Columns null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null,  //having
                null //orderBy
        );
        return new ChatCursorWrapper(cursor);
    }

}

