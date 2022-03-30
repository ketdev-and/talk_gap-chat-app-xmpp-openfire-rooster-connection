package com.blikoon.roosterplus.model;

import android.content.ContentValues;

/**
 *  created by everest on March/ 11 / 2021
 */

public class Contact {
    private  String jid;
    private SubscriptionType subscriptionType;
    private String profileImagePath;
    private int persistID;

    public static final String TABLE_NAME = "contacts"; //for use in persistance
   // you can also include the users CHAT Background-image to the table
    public static final class  Cols
    {
        public static  final String CONTACT_UNIQUE_ID = "contactUniqueID";
        public static  final String CONTACT_JID = "jid";
        public static  final String SUBSCRIPTION_TYPE = "subscriptionType";
        public static final String PROFILE_IMAGE_PATH = "profileImagePath";

    }


    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();
        //CONTACT_UNIQUE_ID is auto-filed
        values.put(Cols.CONTACT_JID, jid);
        values.put(Cols.SUBSCRIPTION_TYPE, getTypeStringValue(subscriptionType));
        values.put(Cols.PROFILE_IMAGE_PATH, profileImagePath);

        return  values;
    }


    public  String getTypeStringValue (SubscriptionType type)
    {
        if (type == SubscriptionType.NONE_NONE)
            return  "NONE_NONE";
        else if (type == SubscriptionType.NONE_PENDING)
            return  "NONE_PENDING";
        else if (type == SubscriptionType.NONE_TO)
            return "NONE_TO";
        else if (type == SubscriptionType.PENDING_NONE)
            return "PENDING_NONE";
        else if (type == SubscriptionType.PENDING_PENDING)
            return "PENDING_PENDING";
        else if (type == SubscriptionType.PENDING_TO)
            return "PENDING_TO";
        else if (type == SubscriptionType.FROM_NONE)
            return "FROME_NONE";
        else if (type == SubscriptionType.FROM_PENDING)
            return "FROM_PENDING";
        else if (type == SubscriptionType.FROM_TO)
            return "FROM_TO";
        else
            return  "INDETERMINATE";
    }




    public enum SubscriptionType{
      // Subscription type should catter for the from and to the channels. we should simultaneously known
      //FROM - TO
      NONE_NONE, // No presence subscription
         NONE_PENDING,
      NONE_TO,

        PENDING_NONE,
        PENDING_PENDING,
        PENDING_TO,

        FROM_NONE,
        FROM_PENDING,
        FROM_TO


    }

    public Contact (String jid, SubscriptionType subscriptionType){
        this.jid = jid;
        this.subscriptionType = subscriptionType;
        this.profileImagePath = "NONE"; // TO SET THE PROFILE IMAGE PATH TO NONE, IF YOU DON'T HAVE PROFILE IMAGE SET.YOU CAN ALSO DO THE SAME FROM USER BACKGROUD CHAT.
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }


    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public int getPersistID() {
        return persistID;
    }

    public void setPersistID(int persistID) {
        this.persistID = persistID;
    }
}
