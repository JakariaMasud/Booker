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

import com.example.booker.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FragmentHomeBinding homeBinding;
    SharedPreferences preferences;
    User user;



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
        preferences=this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData= preferences.contains("userData");
        if(hasData){
            Gson gson = new Gson();
            String json = preferences.getString("userData", "");
            user = gson.fromJson(json, User.class);
        homeBinding.welcomeTV.setText("Welcome  "+user.getName());
        }

    }
}
