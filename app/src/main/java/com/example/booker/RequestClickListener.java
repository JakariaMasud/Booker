package com.example.booker;

import android.view.View;

public interface RequestClickListener {
    void onRequestClick(int position, View v);
    void onRequestLongClick(int position, View v);
}
