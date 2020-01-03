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

import com.example.booker.databinding.FragmentBookRequestBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


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
DatabaseReference databaseReference;


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
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Book_requests/");
        requestList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        adapter=new BookRequestAdapter(requestList,userId);
        requestBinding.bookRequestRV.setLayoutManager(layoutManager);
        requestBinding.bookRequestRV.setAdapter(adapter);
        prepareAllData();


    }

    private void prepareAllData() {
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
