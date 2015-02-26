package com.mma.calendar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.constants.Constants;
import com.mma.calendar.model.Event;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class NotificationDetails extends Activity {

    private TextView txtEventTitle;
    private TextView txtEventDescription;
    private TextView txtEventLocation;
    private TextView txtEventStartDuration;
    private TextView txtEventEndDuration;
    private Button btnShowLocation;
    private ListView list;

    private Event eventInfo;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Parse.initialize(this, "DCKoMDwof5x093rjDrebXmYKSz6jaUhX8pR7GZwO", "wItuxyAckUKIMTpmbHqYeRIbvLx9Zl25kTrO3GMF");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Event.class);

        init();
        showDetails();
    }

    private void init() {
        txtEventTitle = (TextView) findViewById(R.id.txt_event_details_title);
        txtEventDescription = (TextView) findViewById(R.id.txt_event_details_description);
        txtEventLocation = (TextView) findViewById(R.id.txt_event_details_location);
        txtEventStartDuration = (TextView) findViewById(R.id.txt_event_details_start);
        txtEventEndDuration = (TextView) findViewById(R.id.txt_event_details_end);
        list = (ListView) findViewById(R.id.list_invited_user);

        btnShowLocation = (Button) findViewById(R.id.btn_show_map);
        Spannable lblButton = new SpannableString(" Show Map");
        lblButton.setSpan(new ImageSpan(getApplicationContext(), R.drawable.btn_location, ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnShowLocation.setText(lblButton);
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


        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDetails() {
        Intent intent = getIntent();
        String getEventID = intent.getStringExtra(Constants.OBJECT_ID);

        adapter = new ArrayAdapter<String>(NotificationDetails.this, android.R.layout.simple_list_item_1);

        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectId", getEventID);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    for (Event event : events) {
                        eventInfo = event;
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

                        if (event.getInviteUserList() != null) {
                            for (int i = 0; i < event.getInviteUserList().size(); i++) {
                                try {
                                    event.getInviteUserList().get(i).fetchIfNeeded();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                adapter.add(event.getInviteUserList().get(i).getUsername());
                            }
                        } else {
                            adapter.add("");
                        }
                    }
                }
            }
        });

        list.setAdapter(adapter);
    }

    public void showMap (final View v) {
        if (eventInfo.getAddress().length() > 0) {

            String address = eventInfo.getAddress();
            double lat = eventInfo.getLat();
            double lon = eventInfo.getLot();


            LocationManager m = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = m.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = m.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            double clat = loc.getLatitude();
            double clon = loc.getLongitude();

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + clat  + "," + clon+ "&daddr=" + lat+ "," + lon ));
            startActivity(intent);

        } else {
            Toast.makeText(NotificationDetails.this, "This event doesn't have location!", Toast.LENGTH_LONG).show();
        }
    }
}
