package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.booker.databinding.FragmentHomeBinding;
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
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentHomeBinding homeBinding;
    SharedPreferences preferences;
    private DatabaseReference databaseReference;
    List<Book> bookList;
    RecyclerView.LayoutManager layoutManager;
    BookAdapterHome adapter;
    String user_key;
    NavController navController;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return homeBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        layoutManager = new LinearLayoutManager(getActivity());
        bookList = new ArrayList<>();
        adapter = new BookAdapterHome(bookList);
        homeBinding.allBooksRV.setLayoutManager(layoutManager);
        homeBinding.allBooksRV.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Books");

        preferences = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData = preferences.contains("user_key");
        if (hasData) {
            user_key = preferences.getString("user_key", "");

            PreparingAllData();
            settingUpListView();

        }


    }

    private void PreparingAllData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot singleBook : dataSnapshot.getChildren()) {
                    Book book = singleBook.getValue(Book.class);
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
                String bookId = bookList.get(position).getBookId();
                HomeFragmentDirections.ActionHomeToBookDetailsFragment action = HomeFragmentDirections.actionHomeToBookDetailsFragment();
                action.setBookId(bookId);
                navController.navigate(action);


            }

            @Override
            public void onBookLongClick(int position, View v) {


            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

