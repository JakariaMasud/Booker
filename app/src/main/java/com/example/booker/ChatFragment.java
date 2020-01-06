package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentChatBinding;
import com.example.booker.databinding.SingleChatItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    FragmentChatBinding chatBinding;
    DatabaseReference chatRef,userRef;
    SharedPreferences sharedPreferences;
    static String userId;
    static NavController navController;
    List<Message>messageList;
    List<User> userList;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference chatListRef;
    RecyclerView.LayoutManager layoutManager;





    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);
        return chatBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        sharedPreferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userId= sharedPreferences.getString("user_key",null);
        chatListRef=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Chats");
        messageList=new ArrayList<>();
        userList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        userRef=FirebaseDatabase.getInstance().getReference("Users");
        chatBinding.chatUserListRV.setLayoutManager(layoutManager);



    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Info> options=new FirebaseRecyclerOptions.Builder<Info>()
                .setQuery(chatListRef,Info.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Info,ChatViewHolder>(options){

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
                SingleChatItemBinding binding= DataBindingUtil
                        .inflate(layoutInflater,R.layout.single_chat_item,parent,false);
                return new ChatViewHolder(binding);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Info model) {
                Log.e("model",model.toString());
                final String KEY=getRef(position).getKey();
                holder.itemBinding.lastMsgTV.setText(model.getLast_Msg().getMessage());
                holder.itemBinding.chatUsernameTV.setText(model.getName());
                if(model.getProfilePicLink()!=null){
                    Picasso.get().load(model.getProfilePicLink())
                            .placeholder(R.drawable.user_profile).into(holder.itemBinding.chatProfileIV);
                }
                holder.itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatFragmentDirections.ActionChatToMessageFragment action=ChatFragmentDirections.actionChatToMessageFragment(KEY);
                        navController.navigate(action);
                    }
                });


            }
        };
        chatBinding.chatUserListRV.setAdapter(adapter);
        adapter.startListening();

    }
    public  static  class ChatViewHolder extends RecyclerView.ViewHolder{
        SingleChatItemBinding itemBinding;

        public ChatViewHolder(@NonNull SingleChatItemBinding itemView) {
            super(itemView.getRoot());
            itemBinding=itemView;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
