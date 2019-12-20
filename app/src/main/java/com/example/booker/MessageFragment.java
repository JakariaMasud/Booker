package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentMessageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding messageBinding;
    DatabaseReference databaseReference;
    String recieverId,senderId;
    SharedPreferences preferences;
    MessageAdapter adapter;
    List<Message> messageList=new ArrayList();

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
        recieverId=MessageFragmentArgs.fromBundle(getArguments()).getRecieverId();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        adapter=new MessageAdapter(messageList,senderId);
        messageBinding.messageRV.setLayoutManager(new LinearLayoutManager(getContext()));
        messageBinding.messageRV.setAdapter(adapter);

        messageBinding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageBinding.messageET.getText().toString();
                if(TextUtils.isEmpty(message)){
                    messageBinding.messageET.setError("please enter a message");
                    return;
                }
                else {
                    String msgKey=databaseReference.child("Chats").child(senderId).child(recieverId).push().getKey();
                    Map messageMap=new HashMap<>();
                    messageMap.put("message",message);
                    messageMap.put("timeStamp",ServerValue.TIMESTAMP);
                    messageMap.put("senderId",senderId);
                    messageMap.put("type","Text");
                    String senderRef="Chats/"+senderId+"/"+recieverId+"/"+msgKey;
                    String recieverRef="Chats/"+recieverId+"/"+senderId+"/"+msgKey;

                    Map userMap=new HashMap();
                    userMap.put(senderRef,messageMap);
                    userMap.put(recieverRef,messageMap);
                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "message is successfully saved", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child("Chats").child(senderId).child(recieverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message=dataSnapshot.getValue(Message.class);
                messageList.add(message);
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
    }
}
