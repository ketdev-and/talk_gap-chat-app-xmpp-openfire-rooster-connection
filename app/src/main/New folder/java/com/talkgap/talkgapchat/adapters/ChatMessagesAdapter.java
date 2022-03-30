package com.blikoon.roosterplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blikoon.roosterplus.R;
import com.blikoon.roosterplus.model.ChatMessage;
import com.blikoon.roosterplus.model.ChatMessagesModel;

import java.util.List;

/**
 *  created by Everest on March / 09 / 2021
 */


public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessageViewHolder>{

    /**
     * interface to let the view recycler view know that an item an item has been added so it
     * can scroll down.
     */

    public interface  OnInformRecyclerViewToScrollDownListener {
        public void onInformRecyclerViewToScrollDown(int size);
    }



    private static final int SENT = 1;
    private static  final int RECEIVED = 2;
    private static final String LOGTAG = "ChatMessageAdapter";

    private List<ChatMessage> mChatMessageList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String contactJid;
    private OnInformRecyclerViewToScrollDownListener mOnInformRecyclerViewToScrollDownListener;


    public void setmOnInformRecyclerViewToScrollDownListener(OnInformRecyclerViewToScrollDownListener mOnInformRecyclerViewToScrollDownListener) {
        this.mOnInformRecyclerViewToScrollDownListener = mOnInformRecyclerViewToScrollDownListener;
    }

    public ChatMessagesAdapter(Context context, String contactJid)
    {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.contactJid = contactJid;

        mChatMessageList = ChatMessagesModel.get(context).getMessages();




    }

    public void  informRecyclerViewToScrollDown()
    {
        mOnInformRecyclerViewToScrollDownListener.onInformRecyclerViewToScrollDown(mChatMessageList.size());
    }



    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType)
        {
            case  SENT:
                    itemView = mLayoutInflater.inflate(R.layout.chat_message_sent, parent, false);
                    return  new ChatMessageViewHolder(itemView);
            case RECEIVED:
                   itemView = mLayoutInflater.inflate(R.layout.chat_message_received, parent, false);
                  return  new ChatMessageViewHolder(itemView);
            default:
                   itemView = mLayoutInflater.inflate(R.layout.chat_message_sent, parent, false);
                   return  new ChatMessageViewHolder(itemView);


        }



    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
       ChatMessage chatMessage = mChatMessageList.get(position);
       holder.bindChat(chatMessage);

    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * this code below help us control how to display the message type , SEND MESSAGE, RECEIVED MESSAGE
         * you can also add more message type and how to display each massage type: Audio, video, pictures (video sent or video received)
         */

        ChatMessage.Type messageType = mChatMessageList.get(position).getType();
        if (messageType == ChatMessage.Type.SENT)
        {
            return SENT;
        } else {
            return RECEIVED;
        }

    }

    public void onMessageAdd() {
        mChatMessageList = ChatMessagesModel.get(mContext).getMessages();
        notifyDataSetChanged();
        informRecyclerViewToScrollDown();

    }
}


class ChatMessageViewHolder extends RecyclerView.ViewHolder{

    private TextView mMessageBody, mMessageTimestamp;
    private ImageView profileImage;

    public ChatMessageViewHolder( View itemView) {
        super(itemView);

        mMessageBody = (TextView) itemView.findViewById(R.id.text_message_body);
        mMessageTimestamp = (TextView) itemView.findViewById(R.id.text_message_timestamp);
        profileImage = (ImageView) itemView.findViewById(R.id.profile);

    }
    public void bindChat (ChatMessage chatMessage)
    {
        mMessageBody.setText(chatMessage.getMessage());
        mMessageTimestamp.setText(chatMessage.getFormattedTime());
        profileImage.setImageResource(R.drawable.ic_profile_image);
    }




}