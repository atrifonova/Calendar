package com.mma.calendar.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.adapters.CalendarMonthAdapter;
import com.mma.calendar.database.DataSource;
import com.mma.calendar.model.User;
import com.mma.calendar.util.Utility;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends ActionBarActivity {

    final private static int DIALOG_LOGIN = 1;

    private DataSource dataSource;

    private EditText inputUserName;
    private EditText inputPassword;
    private Button loginButton;
    private Button cancelButton;

    private String userName;
    private String password;


    private GregorianCalendar month, itemmonth;// calendar instances.

    private CalendarMonthAdapter adapter;// adapter instance
    private Handler handler;// for grabbing some event values for showing the dot
    // marker.
    private ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    private ArrayList<String> event;
    private LinearLayout rLayout;
    private ArrayList<String> date;
    private ArrayList<String> desc;

    private Runnable calendarUpdater;

    public CalendarActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_month_view);


        dataSource = new DataSource(CalendarActivity.this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        init();

    }


    private void init() {
        rLayout = (LinearLayout) findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarMonthAdapter(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        initCalendarUpdater();
        handler.post(calendarUpdater);


        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarMonthAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarMonthAdapter.dayString.get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking

                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarMonthAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(Utility.nameOfEvent.get(i));
                    }
                }

                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(CalendarActivity.this);

                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }

                desc = null;

            }

        });


    }

    private void initCalendarUpdater() {
        calendarUpdater = new Runnable() {

            @Override
            public void run() {
                Log.e("", "calendarUpdater");
                items.clear();

                // Print dates of the current week
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String itemvalue;
                event = Utility.readCalendarEvent(getApplicationContext());
                Log.d("=====Event====", event.toString());
                Log.d("=====Date ARRAY====", Utility.startDates.toString());

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    itemvalue = df.format(itemmonth.getTime());
                    itemmonth.add(GregorianCalendar.DATE, 1);
                    items.add(Utility.startDates.get(i).toString());
                }
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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
        if (id == R.id.action_login) {
            showDialog(DIALOG_LOGIN);
        }
        if (id == R.id.action_register) {
            Intent intent = new Intent(CalendarActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialogDetails = null;

        switch (id) {
            case DIALOG_LOGIN:
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogView = inflater.inflate(R.layout.dialog_login_layout, null);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Log In");
                dialogBuilder.setView(dialogView);
                dialogDetails = dialogBuilder.create();

                break;
        }
        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_LOGIN:
                final AlertDialog alertDialog = (AlertDialog) dialog;
                loginButton = (Button) alertDialog.findViewById(R.id.btn_log_in);
                cancelButton = (Button) alertDialog.findViewById(R.id.btn_cancel);
                inputUserName = (EditText) alertDialog.findViewById(R.id.txt_user_name_login);
                inputPassword = (EditText) alertDialog.findViewById(R.id.txt_user_password_login);

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        userName = inputUserName.getText().toString();
                        password = inputPassword.getText().toString();

                        List<User> values = dataSource.getAllUsers();

                        if (values == null) {
                            Toast.makeText(CalendarActivity.this, "No records", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < values.size(); i++) {
                                if (values.get(i).getUserName().equals(userName) && values.get(i).getUserPassword().equals(password)) {
                                    Toast.makeText(CalendarActivity.this, "Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                break;
        }
    }


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }
}
