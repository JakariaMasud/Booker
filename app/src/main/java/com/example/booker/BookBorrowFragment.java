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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentBookBorrowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookBorrowFragment extends Fragment {
    FragmentBookBorrowBinding borrowBinding;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;
    List<Book> borrowedBookList;
    RecyclerView.LayoutManager layoutManager;
    BookBorrowAdapter adapter;
    String user_key;
    NavController navController;
    User user;
    Book book;



    public BookBorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        borrowBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book_borrow, container, false);
        return borrowBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        navController= Navigation.findNavController(view);
        layoutManager=new LinearLayoutManager(getActivity());
        borrowedBookList=new ArrayList<>();
        adapter=new BookBorrowAdapter(borrowedBookList);
        borrowBinding.borrowBooksRV.setLayoutManager(layoutManager);
        borrowBinding.borrowBooksRV.setAdapter(adapter);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            user_key=preferences.getString("user_key",null);
            PreparingAllData();
            gettingAllData();







        adapter.setOnBookClickListener(new BookClickListener() {
            @Override
            public void onBookClick(int position, View v) {




            }

            @Override
            public void onBookLongClick(int position, View v) {

            }
        });

    }

    private void sendReturnRequest(int position) {
        if(borrowedBookList.size()>0){
            String key=databaseReference.child(user_key).child("Return_requests").push().getKey();
            Map notificationMap=new HashMap();
            book=borrowedBookList.get(position);
            notificationMap.put("bookTitle",book.getTitle());
            notificationMap.put("requesterName",user.getName());
            notificationMap.put("requestId",key);
            notificationMap.put("requesterId",user_key);
            notificationMap.put("bookId",book.getBookId());
            notificationMap.put("timeStamp", ServerValue.TIMESTAMP);
            notificationMap.put("status",false);
            databaseReference.child("Users").child(book.getOwnerId()).child("Return_requests").child(key).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Return request sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void gettingAllData() {
        databaseReference.child("Users").child(user_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void PreparingAllData() {
        databaseReference.child("Users").child(user_key).child("Books").child("Borrowed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                borrowedBookList.clear();
                for(DataSnapshot singleBook: dataSnapshot.getChildren()){
                    Book book=singleBook.getValue(Book.class);
                    borrowedBookList.add(book);
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

        switch(item.getItemId()) {
            case 210:
                String ownerId=borrowedBookList.get(position).getOwnerId();
                DeskFragmentDirections.ActionDeskFragmentToMessageFragment action=DeskFragmentDirections.actionDeskFragmentToMessageFragment(ownerId);
                navController.navigate(action);
                return true;
            case 211:
                sendReturnRequest(position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
