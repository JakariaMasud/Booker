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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentDeskBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeskFragment extends Fragment {
   private FragmentDeskBinding deskBinding;
   private DatabaseReference databaseReference;
   private SharedPreferences preferences;
   List<Book> bookList;
   RecyclerView.LayoutManager layoutManager;
   BookAdapter adapter;
   String user_key;
   NavController navController;



    public DeskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deskBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_desk, container, false);
        return deskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        layoutManager=new LinearLayoutManager(getActivity());
        bookList=new ArrayList<>();
        adapter=new BookAdapter(bookList);
        deskBinding.booksRV.setLayoutManager(layoutManager);
        deskBinding.booksRV.setAdapter(adapter);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData= preferences.contains("user_key");
        if(hasData){
            user_key = preferences.getString("user_key", "");
        }
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        PreparingAllData();
        settingUpListView();



    }

    private void PreparingAllData() {
        databaseReference.child(user_key).child("Books").child("Available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for(DataSnapshot singleBook: dataSnapshot.getChildren()){
                    Book book=singleBook.getValue(Book.class);
                    bookList.add(book);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void settingUpListView() {
   adapter.setOnBookClickListener(new BookClickListener() {
       @Override
       public void onBookClick(int position, View v) {
           String book_id=bookList.get(position).getBookId();
           DeskFragmentDirections.ActionDeskFragmentToBookDetailsFragment action=DeskFragmentDirections.actionDeskFragmentToBookDetailsFragment();
           action.setBookId(book_id);
           navController.navigate(action);
       }

       @Override
       public void onBookLongClick(int position, View v) {

       }
   });


    }
}
