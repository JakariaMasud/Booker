package com.example.booker;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.databinding.FragmentBookDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {
    FragmentBookDetailsBinding bookDetailsBinding;
    String bookId;
    DatabaseReference databaseReference;
    User owner;
    String ownerId,userId;
    NavController navController;
    SharedPreferences preferences;



    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bookDetailsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book_details, container, false);
        return bookDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userId=preferences.getString("user_key",null);
        navController= Navigation.findNavController(view);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        if(getArguments()!=null){
           bookId= BookDetailsFragmentArgs.fromBundle(getArguments()).getBookId();
          settingUpUi();
          settingUpLisener();

        }



    }

    private void settingUpLisener() {
        bookDetailsBinding.ownerChatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragmentDirections.ActionBookDetailsFragmentToMessageFragment action;
                action = BookDetailsFragmentDirections.actionBookDetailsFragmentToMessageFragment(ownerId);
                navController.navigate(action);


            }
        });
    }

    private void settingUpUi() {
        databaseReference.child("Books").child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isExist=dataSnapshot.exists();
               Book book =dataSnapshot.getValue(Book.class);
                bookDetailsBinding.titleTV.setText("Book Title : "+book.getTitle());
                bookDetailsBinding.authorTV.setText("Author : "+book.getAuthor());
                bookDetailsBinding.editionTV.setText("Edition : "+book.getEdition());
                bookDetailsBinding.publisherTV.setText("Publisher : "+book.getPublisher());
                bookDetailsBinding.genreTV.setText("Book Category : "+book.getGenre());
                bookDetailsBinding.isbnTV.setText("ISBN : "+book.getIsbn());
                bookDetailsBinding.languageTV.setText("Language : "+book.getLanguage());
                bookDetailsBinding.numberOfPageTV.setText("Total Number of Page : "+book.getNumberOfPage());
                bookDetailsBinding.securityMoneyTV.setText("Security Money : "+book.getSecurityMoney());
                Picasso.get().load(book.getCoverLink()).placeholder(R.drawable.book_place_holder).into(bookDetailsBinding.bookDetailIV);
                ownerId=book.getOwnerId();
                if(userId.equals(ownerId)){
                    bookDetailsBinding.ownerChatBTN.setVisibility(View.GONE);
                    bookDetailsBinding.ownerLocationBTN.setVisibility(View.GONE);
                    bookDetailsBinding.paymentBTN.setVisibility(View.GONE);
                    bookDetailsBinding.requestBTN.setVisibility(View.GONE);
                }
                if(ownerId!=null){
                    gettingOwnerInfo();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gettingOwnerInfo( ) {
        databaseReference.child("Users").child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                owner=dataSnapshot.getValue(User.class);
                bookDetailsBinding.ownerTV.setText("Book owner : "+owner.getName());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
