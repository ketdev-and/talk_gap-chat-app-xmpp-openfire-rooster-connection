package com.blikoon.roosterplus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blikoon.roosterplus.R;
import com.blikoon.roosterplus.model.Contact;
import com.blikoon.roosterplus.model.ContactModel;

import java.util.List;

/**
 * created by everest on March / 11 /2021
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactHolder> {

    public interface OnItemClickListener{
        public void onItemClick(String contactJid);
    }

    public interface OnItemLongClickListerner {

        public void onItemLongClick (int  uniqueId, String contactJid, View anchor);
    }


    private List<Contact> mContacts;
    private Context mContext;
    private static final String LOGTAG = "ContactListAdapter";
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListerner mOnItemLongClickListerner;

   public ContactListAdapter(Context context)
   {
       mContacts = ContactModel.get(context).getContact();
       mContext = context;
       Log.d(LOGTAG,"Contructor of ChatListAdapter, the size of the backing list is :" + mContacts);

   }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;

    }

    public OnItemLongClickListerner getmOnItemLongClickListerner() {
        return mOnItemLongClickListerner;
    }

    public void setmOnItemLongClickListerner(OnItemLongClickListerner mOnItemLongClickListerner) {
        this.mOnItemLongClickListerner = mOnItemLongClickListerner;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.contact_list_item, parent,
                        false);

        return new ContactHolder(view, this);

   }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
       Contact contact = mContacts.get(position);
       holder.bindContact(contact);

   }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void onContactCountChange(){
       mContacts = ContactModel.get(mContext).getContact();
       notifyDataSetChanged();
       Log.d(LOGTAG,"ContactListAdapter Knows of the change in messages");
    }

}

class ContactHolder extends RecyclerView.ViewHolder{
   private TextView jidTextView;
   private  TextView subscriptionTypeTextView;
   private Contact mContact;
   private ImageView profile_image;
   private ContactListAdapter mAdapter;
   private  static final  String LOGTAG = "ContactHolder";

    public ContactHolder(@NonNull final View itemView, ContactListAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
        jidTextView = (TextView) itemView.findViewById(R.id.contact_jid_string);
        subscriptionTypeTextView = (TextView) itemView.findViewById(R.id.suscription_type);
        profile_image =  (ImageView) itemView.findViewById(R.id.profile_contact);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGTAG, "calling the Contact Item");
                ContactListAdapter.OnItemClickListener listener = mAdapter.getmOnItemClickListener();

                if ( listener != null )
                {
                    listener.onItemClick(jidTextView.getText().toString());

                    Log.d(LOGTAG, "calling the listerner method");
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ContactListAdapter.OnItemLongClickListerner listener = mAdapter.getmOnItemLongClickListerner();

                if ( listener != null )
                {

                     mAdapter.getmOnItemLongClickListerner().onItemLongClick(mContact.getPersistID(), mContact.getJid(), itemView);
                     return true;
                }

                return false;
            }
        });





    }

    void bindContact (Contact c)
    {
        mContact = c;
        if(mContact == null)
        {
            return;
        }
        jidTextView.setText(mContact.getJid());
        subscriptionTypeTextView.setText("NONE_NONE");
        profile_image.setImageResource(R.drawable.ic_profile_image);
    }







}
