package com.example.booker;

import android.view.View;

public interface BookClickListener {
    void onBookClick(int position, View v);
    void onBookLongClick(int position, View v);
}
