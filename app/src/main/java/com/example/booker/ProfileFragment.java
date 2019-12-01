package com.example.booker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentProfileBinding;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    FragmentProfileBinding profileBinding;
    User user;
    SharedPreferences preferences;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences=this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData= preferences.contains("userData");
        if(hasData){
            Gson gson = new Gson();
            String json = preferences.getString("userData", "");
            user = gson.fromJson(json, User.class);
            profileBinding.nameTV.setText(user.getName());
            profileBinding.occupationTV.setText( "Works as a "+user.getProfession());
            profileBinding.emailTV.setText(user.getEmail());
            profileBinding.phoneTV.setText(user.getPhone());
            profileBinding.locationTV.setText(user.getUserAddress().getAddress());


    }
}}
