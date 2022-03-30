package com.blikoon.roosterplus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.blikoon.roosterplus.adapters.ContactListAdapter;
import com.blikoon.roosterplus.model.Contact;
import com.blikoon.roosterplus.model.ContactModel;

public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.OnItemClickListener, ContactListAdapter.OnItemLongClickListerner {

    private RecyclerView contactListRecyclerView;
    public ContactListAdapter mAdapter;
    private  static final String LOGTAG = "ContactListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton newContactButton = findViewById(R.id.newContactButton);
        newContactButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        newContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });

        contactListRecyclerView = (RecyclerView) findViewById(R.id.contact_list_recycler_view);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        mAdapter = new ContactListAdapter(getApplicationContext());
        mAdapter.setmOnItemClickListener(this);
        mAdapter.setmOnItemLongClickListerner(this);
        contactListRecyclerView.setAdapter(mAdapter);


    }

    private void addContact()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_contact_label_text);
        // Set up the intput
        final EditText input = new EditText(this);
        //Specify the type of input expected; this, for example, sets the input as a password, and word.
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // set up the buttons
        builder.setPositiveButton(R.string.add_contact_contact_confirm_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOGTAG, "user clicked on Ok");
                // to input the contact into the database (SQL) ONCE USER CLICK OK .
                if (ContactModel.get(getApplicationContext()).addContact(new Contact(input.getText().toString(), Contact.SubscriptionType.NONE_NONE)))
                {
                    mAdapter.onContactCountChange();
                    Log.d(LOGTAG, "Contact added successfully");
                }
                  else
                      {
                     Log.d(LOGTAG,"Could not add contact");
                }

            }
        });

        builder.setNegativeButton(R.string.add_contact_cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOGTAG, "User Clicked on Cancel");

                dialog.cancel();
            }
        });
        builder.show();


    }

    @Override
    public void onItemClick(String contactJid) {
        Intent i = new Intent(ContactListActivity.this, ChatViewActivity.class);
       i.putExtra("contact_jid", contactJid);
        startActivity(i);
    }

    @Override
    public void onItemLongClick( final int uniqueId, final String contactJid, View anchor) {
        PopupMenu popup = new PopupMenu(ContactListActivity.this, anchor, Gravity.CENTER);
        // InFlating the popup using xml
     popup.getMenuInflater()
             .inflate(R.menu.contact_list_popup_menu, popup.getMenu());

        //registering popup with OnMenuClickListener
        popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete_contact:
                        if (ContactModel.get(getApplicationContext()).deleteContact(uniqueId)) {
                            mAdapter.onContactCountChange();
                            Toast.makeText(
                                    ContactListActivity.this,
                                    "Contact Deleted Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            break;
                        }

                    case R.id.contact_details:
                        Toast.makeText(
                                ContactListActivity.this,
                                "You Long Cliked to see: " + contactJid + " 's contact details :",
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                }
                return true;
            }


     });
     popup.show();


    }
}