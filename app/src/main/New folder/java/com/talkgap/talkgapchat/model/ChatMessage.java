package com.blikoon.roosterplus.model;

import android.content.ContentValues;
import android.text.format.DateFormat;

import java.util.concurrent.TimeUnit;

/**
 * Created by Everest (from  TalkGap ) on March /09/ 2021.
 */


public class ChatMessage {
    private String message;
    private long timestamps;
    private Type type;
    private String contactJid;
    private int persistID;


    public static final String TABLE_NAME = "ChatsMessages"; // include Is Typing... ( indication
    public static final class  Cols
    {
        public static  final String CHAT_MESSAGE_UNIQUE_ID = "chatMesssageUniqueID";
        public static  final String MESSAGE = "message";
        public static  final String TIMESTAMP = "timestamp";
        public static  final String MESSAGE_TYPE = "messageType";
        public static final String CONTACT_JID = "contactjid";

    }

    public ChatMessage(String message, long timestamps, Type type, String contactJid){

        this.message = message;
        this.timestamps = timestamps;
        this.type = type;
        this.contactJid = contactJid;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContactJid() {
        return contactJid;
    }

    public void setContactJid(String contactJid) {
        this.contactJid = contactJid;
    }

    public int getPersistID() {
        return persistID;
    }

    public void setPersistID(int persistID) {
        this.persistID = persistID;
    }

    /**
     * Creating a medthod that will enable us to display our timestamp as a string value
     * as to enable us to display it
      */

    public String getFormattedTime(){
        long oneDayInMillis = TimeUnit.DAYS.toMillis(1); // 24 * 60 * 60 * 1000;
        long timeDifference = System.currentTimeMillis() - timestamps;

        return timeDifference < oneDayInMillis
                ? DateFormat.format("hh:mm a", timestamps).toString()
                : DateFormat.format("dd MMM - hh:mm a", timestamps).toString();


    }

    public  String getTypeStringValue(Type type)
    {
        if (type == Type.SENT)
            return  "SENT";
        else
            return "RECEIVED";

    }

    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();
        //CONTACT_UNIQUE_ID is auto-filed
        values.put(Cols.MESSAGE, message);
        values.put(Cols.TIMESTAMP, timestamps);
        values.put(Cols.MESSAGE_TYPE, getTypeStringValue(type));
        values.put(Cols.CONTACT_JID, contactJid);

        return  values;
    }



    public enum Type{
       SENT, RECEIVED

    }

}
