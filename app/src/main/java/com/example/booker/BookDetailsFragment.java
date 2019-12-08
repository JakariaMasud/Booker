package com.example.booker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentBookDetailsBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {
    FragmentBookDetailsBinding bookDetailsBinding;


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
    }
}
