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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.booker.databinding.FragmentBookLendBinding;
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
public class BookLendFragment extends Fragment {
    FragmentBookLendBinding lendBinding;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;
    List<Book> lendBookList;
    RecyclerView.LayoutManager layoutManager;
    BookLendAdapter adapter;
    String user_key;
    NavController navController;


    public BookLendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lendBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book_lend, container, false);
        return lendBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        layoutManager=new LinearLayoutManager(getActivity());
        lendBookList=new ArrayList<>();
        adapter=new BookLendAdapter(lendBookList);
        lendBinding.lendbooksRV.setLayoutManager(layoutManager);
        lendBinding.lendbooksRV.setAdapter(adapter);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData= preferences.contains("user_key");
        if(hasData){
            user_key = preferences.getString("user_key", "");
        }
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        PreparingAllData();
        settingUpListener();
    }

    private void settingUpListener() {
        adapter.setOnBookClickListener(new BookClickListener() {
            @Override
            public void onBookClick(int position, View v) {

            }

            @Override
            public void onBookLongClick(int position, View v) {

            }
        });

    }

    private void PreparingAllData() {
        databaseReference.child(user_key).child("Books").child("Lend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lendBookList.clear();
                for(DataSnapshot singleBook: dataSnapshot.getChildren()){
                    Book book=singleBook.getValue(Book.class);
                    lendBookList.add(book);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position=item.getGroupId();
        switch (item.getItemId()){
            case 310:
              String borrowerId=  lendBookList.get(position).getBorrowerId();
              DeskFragmentDirections.ActionDeskFragmentToMessageFragment
                      action=DeskFragmentDirections.actionDeskFragmentToMessageFragment(borrowerId);
              navController.navigate(action);


        }
        return super.onContextItemSelected(item);
    }
}
