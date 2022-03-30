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
import com.blikoon.roosterplus.model.Chat;
import com.blikoon.roosterplus.model.ChatModel;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatHolder> {

    private static final String LOGTAG = "ChatListAdapter" ;

    public interface OnItemClickListener{
       public  void onItemClick(String contactJid);

   }

   public interface OnItemLongClickListener {

       public void onItemLongClick(String contactJid, int chatUniqueId, View anchor);

   }

   List<Chat> chatList;
   private OnItemClickListener mOnItemClickListener;
   private OnItemLongClickListener onItemLongClickListener;
   private Context mContext;


    public ChatListAdapter( Context context) {

        this.chatList = ChatModel.get(context).getChats();
        this.mContext = context;

    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.chat_list_item, parent,
                        false);

        return new ChatHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        Chat chat = chatList.get(position);
        holder.bindChat(chat);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void onChatCountChange(){
        chatList = ChatModel.get(mContext).getChats();
        notifyDataSetChanged();
        Log.d(LOGTAG, "ChatListAdapter Knows of the change in messages");
    }


}

class ChatHolder extends  RecyclerView.ViewHolder{

    private static  final  String LOGTAG = "ChatHolder";
    private TextView contactTextView;
    private TextView messageAbstractTextView;
    private TextView timestampTextView;
    private ImageView profileImage;
    private Chat mChat;
    private ChatListAdapter mChatListAdapter;



    public ChatHolder(@NonNull final View itemView, ChatListAdapter adapter ) {
        super(itemView);

        contactTextView = (TextView) itemView.findViewById(R.id.contact_jid);
        messageAbstractTextView = (TextView) itemView.findViewById(R.id.message_abstract);
        timestampTextView = (TextView) itemView.findViewById(R.id.text_message_timestamp);
        profileImage = (ImageView) itemView.findViewById(R.id.profile);
        mChatListAdapter = adapter;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 ChatListAdapter.OnItemClickListener listener = mChatListAdapter.getmOnItemClickListener();

                 if ( listener != null )
                 {
                     listener.onItemClick(contactTextView.getText().toString());
                 }

                Log.d(LOGTAG, "Click on the item in the recyclerView");
            }
        });

       itemView.setOnLongClickListener(new View.OnLongClickListener() {
       @Override
       public boolean onLongClick(View v) {
           ChatListAdapter.OnItemLongClickListener listener = mChatListAdapter.getOnItemLongClickListener();
          if (listener != null)
           {
                 listener.onItemLongClick(mChat.getJid(), mChat.getPersistID(), itemView);
               return true;
          }
            return false;
         }
     });

    }

    public void  bindChat(Chat chat)
    {
         //you can pass more values and bind it here, such as lastmessage_type, eg(image, text, video audio, pdf, etc)
        //also last_message sender( either you or our contact) and message status( sent, read, pending unread message )
        mChat = chat;
        contactTextView.setText(chat.getJid());
        messageAbstractTextView.setText(chat.getLastMessage());
        profileImage.setImageResource(R.drawable.ic_profile_image);
        timestampTextView.setText("12:12pm");
    }
}


