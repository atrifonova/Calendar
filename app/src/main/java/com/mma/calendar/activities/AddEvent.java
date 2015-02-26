package com.mma.calendar.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.constants.Constants;
import com.mma.calendar.model.Event;
import com.mma.calendar.pickers.CustomTimePickerDialog;
import com.mma.calendar.pickers.MapPicker;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEvent extends Activity implements View.OnClickListener {

    private EditText inputTitle;
    private EditText inputDescription;

    private TextView inputStartDate;
    private TextView inputEndDate;
    private TextView inputStartTime;
    private TextView inputEndTime;
    private MultiAutoCompleteTextView txtAddUsers;
    private EditText txtAddLocation;

    private Button btnCreateEvent;

    SharedPreferences sharedPreferences = null;
    long getDate;

    private List<ParseUser> listOfUser = new ArrayList<ParseUser>();
    private List<String> result = new ArrayList<String>();

    private String currentDateFormat;

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

    private ArrayAdapter<String> adapter;
    private ArrayList<ParseUser> parseUsers = new ArrayList<ParseUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        startDate = new Date();
        endDate = new Date();

        sharedPreferences = getSharedPreferences("CURRENT_DATE", Context.MODE_PRIVATE);

        init();
        inputInAddUser();

    }

    private void init() {

        getDate = sharedPreferences.getLong("KEY", 0L);
        startDate = new Date(getDate);
        endDate = new Date(getDate);
        currentDateFormat = Constants.dateFormat.format(startDate);

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

        txtAddUsers = (MultiAutoCompleteTextView) findViewById(R.id.txt_add_user);

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

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case START_DATE_DIALOG:
                dialog = new DatePickerDialog(this, datePickerListenerStartDate, startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate());
                break;
            case END_DATE_DIALOG:
                dialog = new DatePickerDialog(this, datePickerListenerEndDate, endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate());
                break;
            case START_TIME_DIALOG:
                dialog = new CustomTimePickerDialog(this, startTimePickerListener, hour, minutes, true);
                break;
            case END_TIME_DIALOG:
                dialog = new CustomTimePickerDialog(this, endTimePickerListener, hour, minutes, true);
                break;
        }

        return dialog;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerStartDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            String startDateString = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;

            try {
                startDate = Constants.formatter.parse(startDateString);
                startDate.setHours(startHour);
                startDate.setMinutes(startMinutes);
                startDateString = Constants.dateFormat.format(startDate);

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

            if (validateDate()) {
                try {
                    endDate = Constants.formatter.parse(endDateString);
                    endDateString = Constants.dateFormat.format(endDate);
                    endDate.setHours(endHour);
                    endDate.setMinutes(endMinutes);
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    inputEndDate.setText(endDateString);
                }
            } else {
                inputEndDate.setText(currentDateFormat);
            }

        }
    };

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            startHour = hourOfDay;
            startMinutes = minute;

            startDate.setHours(startHour);
            startDate.setMinutes(startMinutes);

//            if (validateDate()) {
                inputStartTime.setText(new StringBuilder().append(paddingString(startHour)).append(":").append(paddingString(startMinutes)));
//            }
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMinutes = minute;

            endDate.setHours(endHour);
            endDate.setMinutes(endMinutes);


            if (validateDate()) {
                inputEndTime.setText(new StringBuilder().append(paddingString(endHour)).append(":").append(paddingString(endMinutes)));
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_event:
                if (inputTitle.getText().length() > 0) {

                    if (isDateValid) {
                        Event event = new Event();
                        event.setUser(ParseUser.getCurrentUser());
                        event.setTitle(inputTitle.getText().toString());
                        event.setDescription(inputDescription.getText().toString());
                        event.setStartDate(inputStartDate.getText().toString());
                        event.setEndDate(inputEndDate.getText().toString());
                        event.setStartTime(inputStartTime.getText().toString());
                        event.setEndTime(inputEndTime.getText().toString());
                        event.setAddress(txtAddLocation.getText().toString());


                        listOfUser = getParseUsers(txtAddUsers.getText().toString());

                        event.setInviteUserList(listOfUser);

                        event.setLat(lat);
                        event.setLon(lon);
                        event.saveEventually();


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
                intent.putExtra(Constants.ADDRESS, address);

                startActivityForResult(intent, 1);
                break;

            case R.id.btn_add_user:
                showDialog();
                break;
        }
    }

    private List<ParseUser> getParseUsers(String s) {
        ArrayList<ParseUser> list = new ArrayList<ParseUser>();
        ArrayList<String> uNames = new ArrayList<String>();
        for (String name : s.split(",")) {
            uNames.add(name.trim());
        }


        for (ParseUser user : parseUsers) {
            if (uNames.contains(user.getUsername())) {
                list.add(user);
            }
        }


        return list;
    }


    private String paddingString(int c) {
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

            if (endDate.before(startDate)) {
                Toast.makeText(AddEvent.this, "NOT VALID DATE", Toast.LENGTH_SHORT).show();
                isDateValid = false;
            } else {
                isDateValid = true;
            }
        }
        return isDateValid;
    }

    private void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
        adapter = new ArrayAdapter<String>(AddEvent.this, android.R.layout.select_dialog_multichoice);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseUser user : users) {
                        if (!user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                            adapter.add(user.getUsername());
                            parseUsers.add(user);
                        }
                    }
                } else {
                    Log.d("ERROR", e.getMessage());
                }
            }
        });

        builder.setIcon(R.drawable.ic_launcher)
                .setTitle("Select User")
                .setAdapter(adapter, null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder strBuilder = new StringBuilder();
                        for (String str : result) {
                            strBuilder.append(str + " , ");
                        }
                        String users = strBuilder.toString();
                        if (users.contains(",")) {
                            users = users.substring(0, users.lastIndexOf(",")).trim();
                        }
                        txtAddUsers.setText(users);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtAddUsers.setText("");
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
                String selectedUser = textView.getText().toString();
                if (textView.isChecked()) {
//                    result.add(position, listOfUser.get(position).getUsername());
                    result.add(textView.getText().toString());

                } else {
                    if (result.contains(selectedUser)) {
                        result.remove(selectedUser);
                    }
                }
            }
        });

        dialog.show();
    }

    private void inputInAddUser() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

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

        txtAddUsers.setAdapter(adapter);
        txtAddUsers.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }
}
