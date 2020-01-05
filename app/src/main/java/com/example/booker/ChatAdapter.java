package com.example.booker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booker.databinding.SingleChatItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.ChatViewHolder>{
    List <Message> messageList;
    List<User> userList;

    public ChatAdapter( List<Message> messageList, List<User> userList) {
        this.messageList = messageList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        SingleChatItemBinding binding= DataBindingUtil
                .inflate(layoutInflater,R.layout.single_chat_item,parent,false);

        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.chatItemBinding.chatUsernameTV.setText(userList.get(position).getName());
        if(userList.get(position).getProfilePicLink()!=null){
            Picasso.get().load(userList.get(position).getProfilePicLink()).placeholder(R.drawable.user_profile)
                    .into(holder.chatItemBinding.chatProfileIV);
        }
        holder.chatItemBinding.lastMsgTV.setText(messageList.get(position).getMessage());



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public  class ChatViewHolder extends RecyclerView.ViewHolder{
        SingleChatItemBinding chatItemBinding;

        public ChatViewHolder(@NonNull SingleChatItemBinding itemBinding) {
            super(itemBinding.getRoot());
            chatItemBinding=itemBinding;
        }
    }
}
