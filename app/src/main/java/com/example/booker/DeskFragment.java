package com.example.booker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.databinding.FragmentDeskBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeskFragment extends Fragment {
   private FragmentDeskBinding deskBinding;
   private DeskFragmentAdapter adapter;




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
        adapter=new DeskFragmentAdapter(getChildFragmentManager());
        deskBinding.deskViewPager.setAdapter(adapter);
        deskBinding.deskTabLayout.setupWithViewPager(deskBinding.deskViewPager);






    }




}
