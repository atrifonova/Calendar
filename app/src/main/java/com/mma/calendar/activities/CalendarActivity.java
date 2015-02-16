package com.mma.calendar.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarActivity extends ActionBarActivity
        implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex;
    private Calendar currentCalendar;
    private Locale locale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(Date date) {

        robotoCalendarView.markDayAsSelectedDay(date);

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
}