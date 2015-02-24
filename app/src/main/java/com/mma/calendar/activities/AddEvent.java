package com.mma.calendar.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.model.Event;
import com.mma.calendar.services.CalendarReceiver;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEvent extends ActionBarActivity implements View.OnClickListener {

    private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private PendingIntent pendingIntent;

    private EditText inputTitle;
    private EditText inputDescription;

    private TextView inputStartDate;
    private TextView inputEndDate;
    private TextView inputStartTime;
    private TextView inputEndTime;
    private EditText txtAddUsers;
    private EditText txtAddLocation;

    private Button btnCreateEvent;

    SharedPreferences sharedPreferences = null;
    long getDate;

    private String currentDateFormat;
    private Date theDate;


    private int hour;
    private int minutes;
    private boolean isDateValid;

    private ImageButton btn_add_location;
    private ImageView btn_add_users;

    public static final int START_DATE_DIALOG = 1;
    public static final int END_DATE_DIALOG = 2;
    public static final int START_TIME_DIALOG = 3;
    public static final int END_TIME_DIALOG = 4;
    private double lat;
    private double lon;
    private String address;

    private Date startDate;
    private Date endDate;

    private int startMinutes;
    private int startHour;

    private int endMinutes;
    private int endHour;

    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        startDate = new Date();
        endDate = new Date();

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

        btn_add_users = (ImageView) findViewById(R.id.btn_add_user);
        btn_add_users.setOnClickListener(this);

        txtAddUsers = (EditText) findViewById(R.id.txt_add_user);

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
                dialog = new TimePickerDialog(this, startTimePickerListener, hour, minutes, true);
                break;
            case END_TIME_DIALOG:
                dialog = new TimePickerDialog(this, endTimePickerListener, hour, minutes, true);
                break;
        }

        return dialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerStartDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String startDateString = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;

            try {
                startDate = formatter.parse(startDateString);
                startDate.setHours(startHour);
                startDate.setMinutes(startMinutes);
                startDateString = dateFormat.format(startDate);

            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                inputStartDate.setText(startDateString);
            }

            validateDate();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerEndDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String endDateString = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;

            try {
                endDate = formatter.parse(endDateString);
                endDateString = dateFormat.format(endDate);
                endDate.setHours(endHour);
                endDate.setMinutes(endMinutes);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                inputEndDate.setText(endDateString);
            }
            validateDate();
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinutes = minute;

            inputStartTime.setText(new StringBuilder().append(paddingString(startHour)).append(":").append(paddingString(startMinutes)));
            startDate.setHours(startHour);
            startDate.setMinutes(startMinutes);

            validateDate();
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMinutes = minute;

            inputEndTime.setText(new StringBuilder().append(paddingString(endHour)).append(":").append(paddingString(endMinutes)));
            endDate.setHours(endHour);
            endDate.setMinutes(endMinutes);

            validateDate();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_event:
                if (inputTitle.getText().length() > 0) {

                    if (isDateValid) {
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

                        setNotification();

                        Intent intent = new Intent(AddEvent.this, CalendarActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddEvent.this, "End date is before start date!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            case R.id.btn_add_location:
                Intent intent = new Intent(getApplicationContext(), MapPicker.class);
                String address = txtAddLocation.getText().toString();
                intent.putExtra(Constants.ADDRESS,address);

                startActivityForResult(intent, 1);
                break;

            case R.id.btn_add_user:
                showDialog();
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

    private void setNotification() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Calendar calendar =  Calendar.getInstance();

        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 22);

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        long when = calendar.getTimeInMillis();         // notification time
        Intent intent = new Intent(this, CalendarReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            lat = data.getDoubleExtra(Constants.LATITUDE, 0.);
            lon = data.getDoubleExtra(Constants.LONGITUDE, 0.);
            Log.v("##########", "BEFORE");

            Log.v("##########", "" + lat);
            Log.v("##########", "" + lon);
            address = data.getStringExtra(Constants.ADDRESS);
            if (address != null) {
                txtAddLocation.setText(address);
            }
        }

    }//onActivityResult


    private boolean validateDate() {
        if (startDate == null || endDate == null) {
            isDateValid = false;
        } else {
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();
            if (startTime > endTime) {
                // Toast.makeText(AddEvent.this, "NOT VALID DATE", Toast.LENGTH_SHORT).show();
                isDateValid = false;
            } else {
                // Toast.makeText(AddEvent.this, "VALID DATE", Toast.LENGTH_SHORT).show();
                isDateValid = true;
            }
        }
        return isDateValid;
    }

    private void showDialog () {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEvent.this, android.R.layout.select_dialog_multichoice);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseUser user : users) {
                        if (!user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                            adapter.add(user.getUsername());
                            Log.d("userName", user.getUsername());
                        }
                    }
                } else {
                    adapter.add("");
                    Log.d("USER", "NOT FOUND");
                }
            }
        });

        builder.setIcon(R.drawable.ic_launcher)
                .setTitle("Select User")
                .setAdapter(adapter, null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtAddUsers.setText(result);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();

        dialog.getListView().setItemsCanFocus(false);
        dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView textView = (CheckedTextView) view;

                if (textView.isChecked()) {
                    result = result + textView.getText() + " ";
                    Toast.makeText(AddEvent.this, result, Toast.LENGTH_LONG).show();
                } else {
                    result = "";
                }
            }
        });

        dialog.show();
    }
}
