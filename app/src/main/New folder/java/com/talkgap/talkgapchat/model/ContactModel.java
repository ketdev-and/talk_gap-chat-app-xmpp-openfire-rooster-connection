package com.blikoon.roosterplus.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;

import com.blikoon.roosterplus.presistence.ContactCursorWrapper;
import com.blikoon.roosterplus.presistence.DatabaseBackend;

import java.util.ArrayList;
import java.util.List;

public class ContactModel {

    private static final String LOGTAG = "ContactModel";
    private static ContactModel sContactMoel;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static  ContactModel get(Context context)
    {
        if(sContactMoel == null)
        {
           sContactMoel = new ContactModel(context);
        }
        return  sContactMoel;
    }

    private ContactModel(Context context)
    {
       mContext = context;
       mDataBase = DatabaseBackend.getInstance(mContext).getWritableDatabase();



    }

    public  List<Contact>getContact()
    {
        List<Contact> contacts = new ArrayList<>();
        ContactCursorWrapper cursor = queryContacts(null, null);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return contacts;
    }

    private ContactCursorWrapper  queryContacts (String whereClause , String [] whereArgs)
    {
        Cursor cursor = mDataBase.query(
                Contact.TABLE_NAME,
                null, // Colums - null selected all colums
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return  new ContactCursorWrapper(cursor);
    }


    public  boolean addContact(Contact c)
    {
        // TODO: Check if contact already in the db before adding.
        ContentValues values = c.getContentValues();
        if ((mDataBase.insert(Contact.TABLE_NAME, null, values) == -1))
        {
            return false;
        } else
        {
            return true;
        }
    }

    public boolean deleteContact(Contact c)
    {
        int uniqueId = c.getPersistID();
        return deleteContact(uniqueId);
    }


    public boolean deleteContact(int uniqueId)
    {
        int value = mDataBase.delete(Contact.TABLE_NAME, Contact.Cols.CONTACT_UNIQUE_ID +"= ?", new String[] {String.valueOf(uniqueId)});

        if (value == 1)
        {
            Log.d(LOGTAG, "Successfully Deleted a record");
            return  true;
        } else
        {
            Log.d(LOGTAG, "Could not delete record");
            return false;
        }


    }

}
