package com.example.booker;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DeskFragmentAdapter extends FragmentPagerAdapter {
    public DeskFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InDeskFragment();
            case 1:
                return new BookBorrowFragment();
            case 2:
                return new BookLendFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "In Desk";
            case 1:
                return "Borrowed";
            case 2:
                return "Lend";
                default:
                    return null;
        }
    }
}
