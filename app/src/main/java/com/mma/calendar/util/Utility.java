package com.mma.calendar.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by skuller on 2/8/15.
 */

public class Utility {
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

    public static ArrayList<String> readCalendarEvent(Context context) {

        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();

        //read from database

        nameOfEvent.add("test");
        long date = System.currentTimeMillis();

        startDates.add(getDate(Long.parseLong("" + date)));
        endDates.add(getDate(Long.parseLong("" + date)));
        descriptions.add("test");


        return nameOfEvent;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}