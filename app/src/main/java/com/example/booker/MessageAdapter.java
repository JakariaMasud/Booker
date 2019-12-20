package com.example.booker;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.ItemMessageRecievedBinding;
import com.example.booker.databinding.ItemMessageSentBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENDER_VIEW_TYPE = 100;
    private static final int RECIEVER_VIEW_TYPE = 200;
    List<Message> messageList = new ArrayList();
    String senderId;

    public MessageAdapter(List<Message> messageList, String senderId) {
        this.messageList = messageList;
        this.senderId = senderId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderId().equals(senderId)) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECIEVER_VIEW_TYPE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        if (viewType == SENDER_VIEW_TYPE) {
            ItemMessageSentBinding sentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message_sent, parent, false);
            return new SenderMessageViewHolder(sentBinding);
        } else {
            ItemMessageRecievedBinding recievedBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message_recieved, parent, false);
            return new RecieverMessageViewHolder(recievedBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case SENDER_VIEW_TYPE:
                SenderMessageViewHolder senderMessageViewHolder = (SenderMessageViewHolder) holder;
                senderMessageViewHolder.messageSentBinding.senderMessageTV.setText(message.getMessage());
                break;
            case RECIEVER_VIEW_TYPE:
                RecieverMessageViewHolder recieverMessageViewHolder = (RecieverMessageViewHolder) holder;
                recieverMessageViewHolder.messageRecievedBinding.recieverNameTV.setText(message.senderId);
                recieverMessageViewHolder.messageRecievedBinding.recieverMessageTV.setText(message.getMessage());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        ItemMessageSentBinding messageSentBinding;

        public SenderMessageViewHolder(@NonNull ItemMessageSentBinding sentBinding) {
            super(sentBinding.getRoot());
            messageSentBinding = sentBinding;

        }
    }

    public class RecieverMessageViewHolder extends RecyclerView.ViewHolder {
        ItemMessageRecievedBinding messageRecievedBinding;

        public RecieverMessageViewHolder(@NonNull ItemMessageRecievedBinding recievedBinding) {
            super(recievedBinding.getRoot());
            messageRecievedBinding = recievedBinding;
        }
    }

}
