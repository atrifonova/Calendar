package com.mma.calendar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.util.Calendar;

public class AddEvent extends ActionBarActivity implements View.OnClickListener {

    private EditText inputTitle;
    private EditText inputDescription;

    private TextView inputStartDate;
    private TextView inputEndDate;
    private TextView inputStartTime;
    private TextView inputEndTime;

    private Button btnCreateEvent;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    public static final int START_DATE_DIALOG = 1;
    public static final int END_DATE_DIALOG = 2;

    SharedPreferences sharedPreferences = null;
    String getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        sharedPreferences = getSharedPreferences("CURRENT_DATE", Context.MODE_PRIVATE);

        init();

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

    }

    private void init() {
        inputTitle = (EditText) findViewById(R.id.txt_event_title);
        inputDescription = (EditText) findViewById(R.id.txt_event_description);

        inputStartDate = (TextView) findViewById(R.id.txt_start_date);
        getDate = sharedPreferences.getString("KEY", "");
        inputStartDate.setText(getDate);

        inputEndDate = (TextView) findViewById(R.id.txt_end_date);
        getDate = sharedPreferences.getString("KEY", "");
        inputEndDate.setText(getDate);

        inputStartTime = (TextView) findViewById(R.id.txt_start_time);
        inputEndTime = (TextView) findViewById(R.id.txt_end_time);

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
        //Toast.makeText(AddEvent.this, "qwqwq", Toast.LENGTH_LONG).show();
        showDialog(1);
    }

    protected Dialog onCreateDialog (int id) {
        Dialog dialog = null;
        switch (id) {
            case START_DATE_DIALOG:
                dialog = new DatePickerDialog(this, datePickerListenerStartDate, year, month, day);
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

                    Intent intent = new Intent(AddEvent.this, CalendarActivity.class);
                    startActivity(intent);

                    break;
                }
        }
    }
}
