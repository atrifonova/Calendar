package com.mma.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mma.calendar.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    final private static int DIALOG_LOGIN = 1;

    private EditText inputUserName;
    private EditText inputPassword;

    private Button loginButton;
    private Button createRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_layout);

        init();
    }

    public void init() {

        inputUserName = (EditText) findViewById(R.id.txt_user_name_login);
        inputPassword = (EditText) findViewById(R.id.txt_user_password_login);

        loginButton = (Button) findViewById(R.id.btn_log_in);
        loginButton.setOnClickListener(LoginActivity.this);

        createRegistration = (Button) findViewById(R.id.btn_create_registration);
        createRegistration.setOnClickListener(LoginActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log_in:
                v.setEnabled(false);
                ParseUser.logInInBackground(inputUserName.getText().toString(), inputPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.btn_create_registration:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
