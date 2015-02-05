package com.mma.calendar.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.database.DataSource;
import com.mma.calendar.model.User;

import java.sql.SQLException;
import java.util.List;


public class CalendarActivity extends ActionBarActivity {

    final private static int DIALOG_LOGIN = 1;

    private DataSource dataSource;

    private EditText inputUserName;
    private EditText inputPassword;
    private Button loginButton;
    private Button cancelButton;

    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Button launchButton = (Button) findViewById(R.id.btn_login_1);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_LOGIN);
            }
        });

        Button btnRegistration = (Button) findViewById(R.id.btn_registration_1);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        dataSource = new DataSource(CalendarActivity.this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialogDetails = null;

        switch (id){
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
                cancelButton = (Button)alertDialog.findViewById(R.id.btn_cancel);
                inputUserName = (EditText)alertDialog.findViewById(R.id.txt_user_name_login);
                inputPassword = (EditText)alertDialog.findViewById(R.id.txt_user_password_login);

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
}
