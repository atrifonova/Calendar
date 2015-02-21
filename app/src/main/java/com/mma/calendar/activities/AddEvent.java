package com.mma.calendar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEvent extends ActionBarActivity implements View.OnClickListener {

    private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private RobotoCalendarView robotoCalendarView;

    private EditText inputTitle;
    private EditText inputDescription;

    private TextView inputStartDate;
    private TextView inputEndDate;
    private TextView inputStartTime;
    private TextView inputEndTime;
    private EditText txtAddLocation;

    private Button btnCreateEvent;

    SharedPreferences sharedPreferences = null;
    long getDate;

    private String currentDateFormat;
    private Date theDate;

    private int hour;
    private int minutes;

    private ImageButton btn_add_location;

    public static final int START_DATE_DIALOG = 1;
    public static final int END_DATE_DIALOG = 2;
    public static final int START_TIME_DIALOG = 3;
    public static final int END_TIME_DIALOG = 4;
    private double lat;
    private double lon;
    private String address;


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

        btn_add_location = (ImageButton) findViewById(R.id.btn_add_location);
        btn_add_location.setOnClickListener(this);

        txtAddLocation = (EditText) findViewById(R.id.txt_add_location);

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
                dialog = new DatePickerDialog(this, datePickerListenerStartDate, theDate.getYear() + 1900, theDate.getMonth(), theDate.getDate());
                break;
            case END_DATE_DIALOG:
                dialog = new DatePickerDialog(this, datePickerListenerEndDate, theDate.getYear() + 1900, theDate.getMonth(), theDate.getDate());
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

            try {
                Date date = formatter.parse(startDate);
                startDate = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                inputStartDate.setText(startDate);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerEndDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String endDate = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;

            try {
                Date date = formatter.parse(endDate);
                endDate = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                inputEndDate.setText(endDate);
            }
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
                    event.setAddress(txtAddLocation.getText().toString());
                    event.setLat(lat);
                    event.setLon(lon);
                    event.saveEventually();

                    //???
                    //robotoCalendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, theDate);

                    Intent intent = new Intent(AddEvent.this, CalendarActivity.class);
                    startActivity(intent);

                    break;
                }
            case R.id.btn_add_location:
                Intent intent = new Intent(getApplicationContext(), MapPicker.class);
                String address = txtAddLocation.getText().toString();
                intent.putExtra(Constants.ADDRESS,address);

                startActivityForResult(intent, 1);
                break;
        }
    }

    private String paddingString (int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            lat = data.getDoubleExtra(Constants.LATITUDE, 0.);
            lon = data.getDoubleExtra(Constants.LONGITUDE, 0.);
            Log.v("##########", "BEFORE");

            Log.v("##########", ""+lat);
            Log.v("##########", ""+lon);
            address = data.getStringExtra(Constants.ADDRESS);
            if (address != null) {
                txtAddLocation.setText(address);
            }
        }

    }//onActivityResult
}
