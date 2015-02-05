package com.mma.calendar.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Toni on 04-Feb-15.
 */
public class User {
    private static final Pattern VALID_EMAIL_ADDRESS = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
    private long userID;
    private String userName;
    private String userPassword;
    private String userPhoto;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    public User() {
        this(0, "", "", "", "", "", "test@test.com");
    }

    public User(long newUserID, String newUserName, String newUserPassword, String newUserPhoto, String newUserFirstName,
                String newUserLastName, String newUserEmail) {

        setUserID(newUserID);
        setUserName(newUserName);
        setUserPassword(newUserPassword);
        setUserPhoto(newUserPhoto);
        setUserFirstName(newUserFirstName);
        setUserLastName(newUserLastName);
        setUserEmail(newUserEmail);
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String newUserName) {
        if (newUserName != null) {
            userName = newUserName;
        } else {
            userName = "";
        }
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String newUserPassword) {
        if (newUserPassword != null) {
            userPassword = newUserPassword;
        } else {
            userPassword = "";
        }
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        if (userPhoto != null) {
            this.userPhoto = userPhoto;
        } else {
            this.userPhoto = "profile_image.png";
        }
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String newUserFirstName) {
        if (newUserFirstName != null) {
            userFirstName = newUserFirstName;
        } else {
            userFirstName = "";
        }
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String newUserLastName) {
        if (newUserLastName != null) {
            userLastName = newUserLastName;
        } else {
            userLastName = "";
        }
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String newUserEmail) {
        Matcher matcher = VALID_EMAIL_ADDRESS.matcher(newUserEmail);
        if (newUserEmail != null && matcher.find()) {
            userEmail = newUserEmail;
        } else {
            userEmail = "";
        }
    }
}
