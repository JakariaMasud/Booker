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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    String userId;
    String user_key;
    NavController navController;
    List<Message>messageList;
    List<User> userList;
    ChatAdapter adapter;





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
        messageList=new ArrayList<>();
        userList=new ArrayList<>();
        adapter=new ChatAdapter(messageList,userList);
        chatRef= FirebaseDatabase.getInstance().getReference("Chats").child(userId);
        userRef=FirebaseDatabase.getInstance().getReference("Users");
        chatBinding.chatUserListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        chatBinding.chatUserListRV.setAdapter(adapter);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){

                    final String key=item.getKey();
                    Query lastQuery=chatRef.child(key).orderByKey().limitToLast(1);
                    lastQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Message message=dataSnapshot.getValue(Message.class);

                            userRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   User user= dataSnapshot.getValue(User.class);
                                   messageList.add(message);
                                   userList.add(user);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    adapter.notifyDataSetChanged();

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
