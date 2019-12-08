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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FragmentHomeBinding homeBinding;
    SharedPreferences preferences;
    private DatabaseReference databaseReference;
    List<Book> bookList;
    RecyclerView.LayoutManager layoutManager;
    BookAdapter adapter;
    String user_key;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager=new LinearLayoutManager(getActivity());
        bookList=new ArrayList<>();
        preferences=this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData= preferences.contains("user_key");
        if(hasData){
            user_key = preferences.getString("user_key", "");

        }
        databaseReference= FirebaseDatabase.getInstance().getReference("Books");
        PreparingAllData();

    }

    private void PreparingAllData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for(DataSnapshot singleBook: dataSnapshot.getChildren()){
                    Book book=singleBook.getValue(Book.class);
                    bookList.add(book);
                }
                settingUpListView();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void settingUpListView() {
        adapter=new BookAdapter(bookList);
        homeBinding.allBooksRV.setLayoutManager(layoutManager);
        homeBinding.allBooksRV.setAdapter(adapter);
    }
}
