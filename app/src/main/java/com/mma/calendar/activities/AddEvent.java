package com.mma.calendar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEvent extends ActionBarActivity implements View.OnClickListener {

    private RobotoCalendarView robotoCalendarView;

    private EditText inputTitle;
    private EditText inputDescription;

    private TextView inputStartDate;
    private TextView inputEndDate;
    private TextView inputStartTime;
    private TextView inputEndTime;

    private Button btnCreateEvent;

    SharedPreferences sharedPreferences = null;
    long getDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private String currentDateFormat;
    private Date theDate;

    private int hour;
    private int minutes;

    public static final int START_DATE_DIALOG = 1;
    public static final int END_DATE_DIALOG = 2;
    public static final int START_TIME_DIALOG = 3;
    public static final int END_TIME_DIALOG = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        sharedPreferences = getSharedPreferences("CURRENT_DATE", Context.MODE_PRIVATE);

        init();

    }

    private void init() {

        getDate = sharedPreferences.getLong("KEY", 0L);
        theDate = new Date(getDate);
        currentDateFormat = dateFormat.format(theDate);

        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        inputTitle = (EditText) findViewById(R.id.txt_event_title);
        inputDescription = (EditText) findViewById(R.id.txt_event_description);

        inputStartDate = (TextView) findViewById(R.id.txt_start_date);
        inputStartDate.setText(currentDateFormat);

        inputEndDate = (TextView) findViewById(R.id.txt_end_date);
        inputEndDate.setText(currentDateFormat);

        inputStartTime = (TextView) findViewById(R.id.txt_start_time);
        inputStartTime.setText(new StringBuilder().append(paddingString(hour)).append(":").append(paddingString(minutes)));

        inputEndTime = (TextView) findViewById(R.id.txt_end_time);
        inputEndTime.setText(new StringBuilder().append(paddingString(hour)).append(":").append(paddingString(minutes)));

        btnCreateEvent = (Button) findViewById(R.id.btn_create_event);
        btnCreateEvent.setOnClickListener(AddEvent.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    public void showStartDate(View v) {
        showDialog(1);
    }

    public void showEndDate(View v) {
        showDialog(2);
    }

    public void showStartTime(View v) {
        showDialog(3);
    }

    public void showEndTime(View v) {
        showDialog(4);
    }

    protected Dialog onCreateDialog (int id) {
        Dialog dialog = null;
        switch (id) {
            case START_DATE_DIALOG:

                //???
                dialog = new DatePickerDialog(this, datePickerListenerStartDate, 2015, theDate.getMonth(), theDate.getDay());
                break;
            case END_DATE_DIALOG:
                //???
                dialog = new DatePickerDialog(this, datePickerListenerEndDate, 2015, theDate.getMonth(), theDate.getDay());
                break;
            case START_TIME_DIALOG:
                dialog = new TimePickerDialog(this, startTimePickerListener, hour, minutes,false);
                break;
            case END_TIME_DIALOG:
                dialog = new TimePickerDialog(this, endTimePickerListener, hour, minutes,false);
                break;
        }

        return dialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerStartDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String startDate = selectedDay + "." + ( selectedMonth + 1 ) + "." + selectedYear;
            inputStartDate.setText(startDate);

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerEndDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String startDate = selectedDay + "." + ( selectedMonth + 1 ) + "." + selectedYear;
            inputEndDate.setText(startDate);

        }
    };

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour = hourOfDay;
            minutes = minute;

            inputStartTime.setText(new StringBuilder().append(paddingString(hour)).append(":").append(paddingString(minute)));
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour = hourOfDay;
            minutes = minute;

            inputEndTime.setText(new StringBuilder().append(paddingString(hour)).append(":").append(paddingString(minute)));
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_event:
                if (inputTitle.getText().length() > 0) {
                    Event event = new Event();
                    event.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    event.setUser(ParseUser.getCurrentUser());
                    event.setTitle(inputTitle.getText().toString());
                    event.setDescription(inputDescription.getText().toString());
                    event.setStartDate(inputStartDate.getText().toString());
                    event.setEndDate(inputEndDate.getText().toString());
                    event.setStartTime(inputStartTime.getText().toString());
                    event.setEndTime(inputEndTime.getText().toString());
                    event.saveEventually();

                    //???
                    //robotoCalendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, theDate);

                    Intent intent = new Intent(AddEvent.this, CalendarActivity.class);
                    startActivity(intent);

                    break;
                }
        }
    }

    private String paddingString (int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }
}
