package com.mma.calendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mma.calendar.R;
import com.mma.calendar.constants.Constants;
import com.mma.calendar.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class NotificationDetails extends Activity {

    private TextView txtEventTitle;
    private TextView txtEventDescription;
    private TextView txtEventLocation;
    private TextView txtEventStartDuration;
    private TextView txtEventEndDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        init();
        showDetails();
    }

    private void init() {
        txtEventTitle = (TextView) findViewById(R.id.txt_event_details_title);
        txtEventDescription = (TextView) findViewById(R.id.txt_event_details_description);
        txtEventLocation = (TextView) findViewById(R.id.txt_event_details_location);
        txtEventStartDuration = (TextView) findViewById(R.id.txt_event_details_start);
        txtEventEndDuration = (TextView) findViewById(R.id.txt_event_details_end);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

    public void showDetails() {
        Intent intent = getIntent();
        String getEventID = intent.getStringExtra(Constants.OBJECT_ID);

        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("objectId", getEventID);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    for (Event event : events) {

                        txtEventTitle.setText(event.getTitle());

                        if (event.getStartDate().equals("") || event.getStartTime().equals("")) {
                            txtEventStartDuration.setText("");
                        } else {
                            txtEventStartDuration.setText(event.getStartDate() + " - " + event.getStartTime());
                        }

                        if (event.getEndDate().equals("") || event.getEndTime().equals("")) {
                            txtEventEndDuration.setText("");
                        } else {
                            txtEventEndDuration.setText(event.getStartDate() + " - " + event.getStartTime());
                        }

                        if (event.getDescription().equals("")) {
                            txtEventDescription.setText("");
                        } else {
                            txtEventDescription.setText(event.getDescription());
                        }

                        if (event.getAddress().equals("")) {
                            txtEventLocation.setText("");
                        } else {
                            txtEventLocation.setText(event.getAddress());
                        }
                    }
                }
            }
        });
    }
}