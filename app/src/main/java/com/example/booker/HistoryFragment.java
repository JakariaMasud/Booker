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

import com.example.booker.databinding.FragmentHistoryBinding;
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
public class HistoryFragment extends Fragment {
    FragmentHistoryBinding historyBinding;
    DatabaseReference databaseReference;
    HistoryAdapter adapter;
    List <History> historyList;
    LinearLayoutManager layoutManager;
    SharedPreferences preferences;
    String user_key;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        historyBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_history, container, false);
        return historyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        user_key=preferences.getString("user_key",null);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        layoutManager=new LinearLayoutManager(getContext());
        historyList=new ArrayList<>();
        adapter=new HistoryAdapter(historyList);
        historyBinding.historyRV.setLayoutManager(layoutManager);
        historyBinding.historyRV.setAdapter(adapter);
        preparingAllData();


    }

    private void preparingAllData() {
        databaseReference.child(user_key).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    History singleHistory=item.getValue(History.class);
                    historyList.add(singleHistory);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
