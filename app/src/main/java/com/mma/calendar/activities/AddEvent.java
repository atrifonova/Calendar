package com.mma.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    final Calendar cal = Calendar.getInstance();
    int dd = cal.get(Calendar.DAY_OF_MONTH);
    int mm = cal.get(Calendar.MONTH);
    int yy = cal.get(Calendar.YEAR);

//    private ImageButton btnAddLocation;
//    private ImageButton btnAddPeople;
    private Button btnCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        init();

    }

    private void init () {
        inputTitle = (EditText) findViewById(R.id.txt_event_title);
        inputDescription = (EditText) findViewById(R.id.txt_event_description);

        inputStartDate = (TextView) findViewById(R.id.txt_start_date);
        inputStartDate.setText(new StringBuilder()
                .append(dd).append(" . ")
                .append(mm + 1).append(" . ")
                .append(yy));

        inputEndDate = (TextView) findViewById(R.id.txt_end_date);
        inputEndDate.setText(new StringBuilder()
                .append(dd).append(" . ")
                .append(mm + 1).append(" . ")
                .append(yy));

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_event:
                if (inputTitle.getText().length() > 0) {
                    Event event = new Event();
                    event.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    event.setUser(ParseUser.getCurrentUser());
                    event.setTitle(inputTitle.toString());
                    event.setDescription(inputDescription.toString());
                    event.setStartDate(inputStartDate.toString());
                    event.setEndDate(inputEndDate.toString());
                    event.setStartTime(inputStartTime.toString());
                    event.setEndTime(inputEndTime.toString());
                    event.saveEventually();

                    Intent intent = new Intent(AddEvent.this, CalendarActivity.class);
                    startActivity(intent);
                }
        }
    }
}
