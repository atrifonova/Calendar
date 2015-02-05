package com.mma.calendar.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mma.calendar.R;
import com.mma.calendar.database.DataSource;
import com.mma.calendar.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;

public class RegistrationActivity extends ActionBarActivity implements View.OnClickListener{

    private final int SELECT_PHOTO = 1;

    private Button btnRegistration;
    private EditText inputUserName;
    private EditText inputPassword;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private ImageButton inputImage;

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private byte[] profileImage;

    private DataSource dataSource;


    private User users = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        dataSource = new DataSource(RegistrationActivity.this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init () {
        btnRegistration = (Button) findViewById(R.id.btn_registration);
        btnRegistration.setOnClickListener(RegistrationActivity.this);

        inputImage = (ImageButton) findViewById(R.id.img_profile);
        inputImage.setOnClickListener(RegistrationActivity.this);

        inputUserName = (EditText) findViewById(R.id.txt_user_name_registration);
        inputPassword = (EditText) findViewById(R.id.txt_user_password_registration);
        inputFirstName = (EditText) findViewById(R.id.txt_first_name);
        inputLastName = (EditText) findViewById(R.id.txt_last_name);
        inputEmail = (EditText) findViewById(R.id.txt_email);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registration:
                userName = inputUserName.getText().toString();
                users.setUserName(userName);

                password = inputPassword.getText().toString();
                users.setUserPassword(password);

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.profile_image);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                profileImage = stream.toByteArray();


                users.setUserPhoto(getBitmapFromAsset("profile_image.png"));

                firstName = inputFirstName.getText().toString();
                users.setUserFirstName(firstName);

                lastName = inputLastName.getText().toString();
                users.setUserLastName(lastName);

                email = inputEmail.getText().toString();
                users.setUserEmail(email);

                users = dataSource.createUser(users.getUserName(), users.getUserPassword(), users.getUserPhoto(), users.getUserFirstName(), users.getUserLastName(), users.getUserEmail());

                Toast.makeText(RegistrationActivity.this, "Successfully", Toast.LENGTH_LONG).show();

                break;
            case R.id.img_profile:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        profileImage = stream.toByteArray();
                        users.setUserPhoto(profileImage);
                        inputImage.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    private byte[] getBitmapFromAsset(String strName) {
        AssetManager assetManager = getApplicationContext().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("img/" + strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        int iBytes = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteBuffer buffer = ByteBuffer.allocate(iBytes);

        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
}
