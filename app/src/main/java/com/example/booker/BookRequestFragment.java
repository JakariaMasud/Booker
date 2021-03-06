package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentBookRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookRequestFragment extends Fragment {
FragmentBookRequestBinding requestBinding;
SharedPreferences sharedPreferences;
String userId;
LinearLayoutManager layoutManager;
BookRequestAdapter adapter;
List<Request> requestList;
DatabaseReference databaseReference,userRef,requestRef;
Map acceptMap;


    public BookRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book_request, container, false);
        return requestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userId=sharedPreferences.getString("user_key",null);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Book_requests");
        requestRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Book_requests");
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        requestList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        adapter=new BookRequestAdapter(requestList);
        requestBinding.bookRequestRV.setLayoutManager(layoutManager);
        requestBinding.bookRequestRV.setAdapter(adapter);
        settingUpListener();
        prepareAllData();



    }

    private void settingUpListener() {
        adapter.setRequestClickListener(new RequestClickListener() {
            @Override
            public void onRequestClick(int position, View v) {
                Log.e("request ","click triggered");
                final Request request=requestList.get(position);
                switch (v.getId()){
                    case R.id.acceptBTN:
                        acceptMap=new HashMap();
                        acceptMap.put("status",true);
                        databaseReference.child(request.getRequestId()).updateChildren(acceptMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "request has been accepted", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        break;


                    case R.id.rejectBTN:
                        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user=dataSnapshot.getValue(User.class);
                                String requesterHistory=user.getName()+" rejected your request for the book "+request.getBookTitle();
                                String recieverHistory="You have rejected the request of "+request.getRequesterName()+" for the book "+request.getBookTitle();
                                String type="request rejected";
                                Map requesterMap=new HashMap();
                                requesterMap.put("historyMsg",requesterHistory);
                                requesterMap.put("historyType",type);
                                Map recieverMap=new HashMap();
                                recieverMap.put("historyMsg",recieverHistory);
                                recieverMap.put("historyType",type);
                                String recieverKey= userRef.child(userId).child("History").push().getKey();
                                String requesterKey=userRef.child(request.getRequesterId()).child("History").push().getKey();
                                Map finalMap=new HashMap();
                                finalMap.put("/"+userId+"/History/"+recieverKey,recieverMap);
                                finalMap.put("/"+request.getRequesterId()+"/History/"+requesterKey,requesterMap);
                                userRef.updateChildren(finalMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            databaseReference.child(request.getRequestId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(), "request Has been rejected successfully", Toast.LENGTH_SHORT).show();
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                }
                                            });

                                        }
                                    }
                                });




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;


                }





            }

            @Override
            public void onRequestLongClick(int position, View v) {

            }
        });

    }

    private void prepareAllData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()){

                    Request reqObj=item.getValue(Request.class);
                    if(!reqObj.status){
                        requestList.add(reqObj);
                    }

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
