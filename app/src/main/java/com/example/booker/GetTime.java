package com.example.booker;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class GetTime {
    public GetTime() {
    }
    public static String getTimeFromTimeStamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm a", cal).toString();
        return date;
    }
}
