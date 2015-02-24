package com.mma.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mma.calendar.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationActivity extends ActionBarActivity{

    private EditText inputUserName;
    private EditText inputPassword;
    private EditText inputEmail;
    private TextView errorField;

    private ImageButton profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

    }

    private void init () {

        inputUserName = (EditText) findViewById(R.id.txt_user_name_registration);
        inputPassword = (EditText) findViewById(R.id.txt_user_password_registration);
        inputEmail = (EditText) findViewById(R.id.txt_email);
        errorField = (TextView) findViewById(R.id.txt_error_messages);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registration(final View v){
        if(inputUserName.getText().length() == 0 || inputPassword.getText().length() == 0 || inputEmail.getText().length() == 0)
            return;

        v.setEnabled(false);
        ParseUser user = new ParseUser();
        user.setUsername(inputUserName.getText().toString());
        user.setPassword(inputPassword.getText().toString());
        user.setEmail(inputEmail.getText().toString());
        errorField.setText("");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(RegistrationActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            errorField.setText("Sorry, this username has already been taken.");
                            break;
                        case ParseException.USERNAME_MISSING:
                            errorField.setText("Sorry, you must supply a username to register.");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            errorField.setText("Sorry, you must supply a password to register.");
                            break;
                    }
                    v.setEnabled(true);
                }
            }
        });
    }
}
