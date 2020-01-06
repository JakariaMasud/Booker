package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentMessageBinding;
import com.example.booker.databinding.ItemMessageRecievedBinding;
import com.example.booker.databinding.ItemMessageSentBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding messageBinding;
    DatabaseReference databaseReference,messageReference,userReference,profileRef;
    String recieverId,senderId,msgSenderId;
    SharedPreferences preferences;
    private static final int SENDER_VIEW_TYPE = 100;
    private static final int RECIEVER_VIEW_TYPE = 200;
    FirebaseRecyclerAdapter<Message,RecyclerView.ViewHolder> adapter;
    Message message;
    private LinearLayoutManager layoutManager;
    String profiePic,userName;
    boolean isRetrived,isChecked;




    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        messageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return messageBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        senderId=preferences.getString("user_key",null);
        layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setReverseLayout(false);
        recieverId=MessageFragmentArgs.fromBundle(getArguments()).getRecieverId();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        userReference= FirebaseDatabase.getInstance().getReference().child("Users");
        messageReference= FirebaseDatabase.getInstance().getReference("Users").child(senderId).child("Chats/").child(recieverId+"/Messages");
        profileRef=FirebaseDatabase.getInstance().getReference("Users");
        retrieveAndCheck();

        messageBinding.messageRV.setLayoutManager(layoutManager);

        messageBinding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageBinding.messageET.getText().toString();
                if(TextUtils.isEmpty(message)){
                    messageBinding.messageET.setError("please enter a message");
                    return;
                }
                else {
                    String msgKey=databaseReference.child(senderId).child("Chats").child(recieverId).child("Messages").push().getKey();
                    Map messageMap=new HashMap<>();
                    messageMap.put("message",message);
                    messageMap.put("timeStamp",ServerValue.TIMESTAMP);
                    messageMap.put("senderId",senderId);
                    messageMap.put("type","Text");
                    String senderRef=senderId+"/Chats/"+recieverId+"/Messages/"+msgKey;
                    String senderRefLast=senderId+"/Chats/"+recieverId+"/Last_Msg";

                    String recieverRef=recieverId+"/Chats/"+senderId+"/Messages/"+msgKey;
                    String recieverRefLast=recieverId+"/Chats/"+senderId+"/Last_Msg";

                    Map userMap=new HashMap();
                    userMap.put(senderRef,messageMap);
                    userMap.put(recieverRef,messageMap);
                    userMap.put(senderRefLast,messageMap);
                    userMap.put(recieverRefLast,messageMap);
                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                messageBinding.messageET.setText("");

                            }

                        }
                    });

                }

            }
        });


    }

    private void retrieveAndCheck() {
        userReference.child(recieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);

                userName=user.getName();
                profiePic=user.getProfilePicLink();
                isRetrived=true;
                if(!isChecked){
                    Map infoMap=new HashMap();
                    infoMap.put("name",userName);
                    infoMap.put("profilePicLink",profiePic);
                    profileRef.child(senderId).child("Chats").child(recieverId).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                isChecked=true;
                                messageBinding.messageRV.setAdapter(adapter);
                                adapter.startListening();
                            }

                        }
                    });

                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Query query =messageReference
                .orderByKey()
                .limitToLast(20);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<Message> options=new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(messageReference,Message.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder>(options) {
            @Override
            public int getItemViewType(int position) {
            message=getItem(position);
            if(message.getSenderId().equals(senderId)){
                return  SENDER_VIEW_TYPE;
            }
            else return RECIEVER_VIEW_TYPE;
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull final Message model) {
                switch (holder.getItemViewType()) {
                    case SENDER_VIEW_TYPE:
                        SenderMessageViewHolder senderMessageViewHolder = (SenderMessageViewHolder) holder;
                        senderMessageViewHolder.messageSentBinding.senderMessageTV.setText(model.getMessage());
                        long time=model.getTimeStamp();
                        senderMessageViewHolder.messageSentBinding.sentTimeTV.setText(GetTime.getTimeFromTimeStamp(time));
                        break;
                    case RECIEVER_VIEW_TYPE:

                        final RecieverMessageViewHolder recieverMessageViewHolder = (RecieverMessageViewHolder) holder;
                        if(isRetrived){
                            Picasso.get().load(profiePic).placeholder(R.drawable.user_profile).into(recieverMessageViewHolder.messageRecievedBinding.recieverIV);
                            recieverMessageViewHolder.messageRecievedBinding.recieverNameTV.setText(userName);
                            recieverMessageViewHolder.messageRecievedBinding.recieverMessageTV.setText(model.getMessage());
                            long time_recieved=model.getTimeStamp();
                            recieverMessageViewHolder.messageRecievedBinding.recievedTimeTV.setText(GetTime.getTimeFromTimeStamp(time_recieved));
                        }




                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + holder.getItemViewType());
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

        };




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

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}
