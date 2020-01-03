package com.example.booker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentRequestBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {
    FragmentRequestBinding requestBinding;
    RequestFragmentAdapter adapter;



    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_request, container, false);
        return requestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new RequestFragmentAdapter(getChildFragmentManager());
        requestBinding.requestViewPager.setAdapter(adapter);
        requestBinding.requestTabLayout.setupWithViewPager(requestBinding.requestViewPager);
    }
}
