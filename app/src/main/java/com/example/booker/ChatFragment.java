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


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    FragmentChatBinding chatBinding;
    DatabaseReference messageRef,userRef,lastMsgRef;
    SharedPreferences sharedPreferences;
    String userId;
    String user_key;
    NavController navController;
    User user;





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

        messageRef= FirebaseDatabase.getInstance().getReference("Chats").child(userId);
        lastMsgRef= FirebaseDatabase.getInstance().getReference("Chats").child(userId);

        userRef=FirebaseDatabase.getInstance().getReference("Users");
        chatBinding.chatUserListRV.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions <User> options=new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(messageRef,User.class)
                .build();
        FirebaseRecyclerAdapter<User,ChatItemViewHolder> adapter=new FirebaseRecyclerAdapter<User, ChatItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatItemViewHolder holder, int position, @NonNull User model) {
                user_key=getRef(position).getKey();
                Query lastQuery = lastMsgRef.child(user_key).orderByKey().limitToLast(1);
                lastQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot lastmsg:dataSnapshot.getChildren()){
                            String message = lastmsg.child("message").getValue().toString();
                            holder.chatItemBinding.lastMsgTV.setText(message);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                userRef.child(user_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         user =dataSnapshot.getValue(User.class);
                        holder.chatItemBinding.chatUsernameTV.setText(user.getName());
                        if(user.getProfilePicLink()==null){

                        }
                        else
                        {
                            Picasso.get().load(user.getProfilePicLink()).into(holder.chatItemBinding.chatProfileIV);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater=LayoutInflater.from(getContext());
                SingleChatItemBinding chatItemBinding=DataBindingUtil.inflate(inflater,R.layout.single_chat_item,parent,false);
                return new ChatItemViewHolder(chatItemBinding);
            }

        };
        chatBinding.chatUserListRV.setAdapter(adapter);
        adapter.startListening();


    }
    public class ChatItemViewHolder extends RecyclerView.ViewHolder {
        SingleChatItemBinding chatItemBinding;
        public ChatItemViewHolder(@NonNull SingleChatItemBinding  item) {
            super(item.getRoot());
            chatItemBinding=item;
            chatItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatFragmentDirections.ActionChatToMessageFragment action=ChatFragmentDirections.actionChatToMessageFragment(user_key);
                    navController.navigate(action);






                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
