package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentReturnRequestBinding;
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
public class ReturnRequestFragment extends Fragment {
    List<Request>requestList;
    private FragmentReturnRequestBinding returnRequestBinding;
    private  ReturnRequestAdapter adapter;
    SharedPreferences sharedPreferences;
    String userId;
    boolean isDone=false;
    LinearLayoutManager layoutManager;
    DatabaseReference databaseReference,userRef,requestRef;
    Map acceptMap;


    public ReturnRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnRequestBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_return_request, container, false);
        return returnRequestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userId=sharedPreferences.getString("user_key",null);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Return_requests");
        requestRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Return_requests");
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        requestList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        adapter=new ReturnRequestAdapter(requestList);
        returnRequestBinding.returnRequestRV.setLayoutManager(layoutManager);
        returnRequestBinding.returnRequestRV.setAdapter(adapter);
        SettingUpListener();
        preparingAllData();



    }

    private void SettingUpListener() {
        adapter.setRequestClickListener(new RequestClickListener() {
            @Override
            public void onRequestClick(int position, View v) {
                final Request request=requestList.get(position);
                switch (v.getId()){
                    case R.id.return_acceptBTN:

                            acceptMap=new HashMap();
                            acceptMap.put("status",true);
                            databaseReference.child(request.getRequestId()).updateChildren(acceptMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "return request has been accepted", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });


                        break;


                    case R.id.return_rejectBTN:

                        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user=dataSnapshot.getValue(User.class);
                                String requesterHistory=user.getName()+" rejected your  return request for the book "+request.getBookTitle();
                                String recieverHistory="You have rejected the return request of "+request.getRequesterName()+" for the book "+request.getBookTitle();
                                String type="return request rejected";
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
                                            requestRef.child(request.getRequestId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(), "return request Has been rejected successfully", Toast.LENGTH_SHORT).show();
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

    private void preparingAllData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    Request request=item.getValue(Request.class);

                    if(!request.status){
                        requestList.add(request);
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
