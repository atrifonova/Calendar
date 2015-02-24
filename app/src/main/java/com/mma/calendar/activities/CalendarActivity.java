package com.mma.calendar.activities;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.mma.calendar.services.CalendarReceiver;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends ActionBarActivity
        implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex;
    private Calendar currentCalendar;
    private Locale locale;

    SharedPreferences sharedPreferences = null;
    long getDate;
    private String currentDateFormat;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        // Gets the calendar from the view
        locale = new Locale("Bulgaria", "BG");
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        // Initialize the RobotoCalendarPicker with the current index and date
        currentMonthIndex = 0;
        currentCalendar = Calendar.getInstance(locale);

        // Mark current day
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

        robotoCalendarView.markDayAsSelectedDay(new Date(System.currentTimeMillis()));

        Parse.initialize(this, "DCKoMDwof5x093rjDrebXmYKSz6jaUhX8pR7GZwO", "wItuxyAckUKIMTpmbHqYeRIbvLx9Zl25kTrO3GMF");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Event.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        sharedPreferences = getSharedPreferences("CURRENT_DATE", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.list_event_title);

        setDateStyle();
        showEvents(new Date(System.currentTimeMillis()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_create_event) {
            Intent intent = new Intent(CalendarActivity.this, AddEvent.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(Date date) {

        robotoCalendarView.markDayAsSelectedDay(date);
        showEvents(date);

    }

    private void showEvents(Date date) {

        getDate = sharedPreferences.getLong("KEY", 0L);
        date = new Date(getDate);
        currentDateFormat = dateFormat.format(date);


        adapter = new ArrayAdapter<String>(CalendarActivity.this, android.R.layout.simple_list_item_1);

        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("startDate", currentDateFormat);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    for (Event event : events) {
                        adapter.add(event.getStartTime() + " - " + event.getEndTime() + " " + event.getTitle());
                    }
                } else {
                    adapter.add("");
                }
            }
        });


        listView.setAdapter(adapter);
    }

    @Override
    public void onRightButtonClick() {
        currentMonthIndex++;
        updateCalendar();
    }

    @Override
    public void onLeftButtonClick() {
        currentMonthIndex--;
        updateCalendar();
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(locale);
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
    }

    private void setDateStyle () {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    for (Event event : events) {
                        try {
                            Date date = dateFormat.parse(event.getStartDate());
                            String startTime = event.getStartTime();
                            int hour = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
                            int minutes = Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1));
                            robotoCalendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, date);
                            date.setHours(hour);
                            date.setMinutes(minutes);
                            Date now = new Date();
                            long nowTime = now.getTime();
                            long eventTime  = date.getTime();
                            if (nowTime < eventTime) {
                                addNotification(date, event.getTitle());
                            }
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

    }


    private void addNotification(Date date, String title) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

/*
        Calendar calendar =  Calendar.getInstance();

        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 22);

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);
*/
//        long when = calendar.getTimeInMillis();         // notification time

        long when = date.getTime();
//        if (when > nowTime) {
        Intent intent = new Intent(this, CalendarReceiver.class);
        intent.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
        //      }
    }
}