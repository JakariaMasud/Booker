package com.example.booker;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.ChatActionBarBinding;
import com.example.booker.databinding.FragmentMessageBinding;

import static android.view.LayoutInflater.from;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding messageBinding;
    private ChatActionBarBinding barBinding;
    ActionBar actionBar;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        messageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return messageBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        barBinding = DataBindingUtil.inflate(layoutInflater, R.layout.chat_action_bar, null, false);
        actionBar.setCustomView(barBinding.getRoot());


    }


}
