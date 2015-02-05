package com.mma.calendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mma.calendar.constants.DBConstants;
import com.mma.calendar.constants.EventConstants;
import com.mma.calendar.constants.UserConstants;
import com.mma.calendar.constants.UserEventConstants;
import com.mma.calendar.model.Event;
import com.mma.calendar.model.User;
import com.mma.calendar.model.UserEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toni on 04-Feb-15.
 */
public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private long userID = 0;
    private long eventID = 0;

    private String allColumnsUserTable[] = {UserConstants.PK_USER_ID, UserConstants.USER_NAME, UserConstants.PASSWORD,
            UserConstants.USER_PHOTO, UserConstants.FIRST_NAME, UserConstants.LAST_NAME, UserConstants.EMAIL};
    private String allColumnsEventTable[] = {EventConstants.PK_EVENT_ID, EventConstants.EVENT_TITLE, EventConstants.EVENT_DESCRIPTION, EventConstants.EVENT_LOCATION,
            EventConstants.EVENT_PHOTO, EventConstants.EVENT_COLOR, EventConstants.EVENT_START_DATE, EventConstants.EVENT_END_DATE, EventConstants.EVENT_START_TIME, EventConstants.EVENT_END_TIME};
    private String allColumnsUserEventTable[] = {UserEventConstants.PK_USER_EVENTS_ID, UserEventConstants.FK_USER_ID, UserEventConstants.FK_EVENT_ID};

    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public User createUser(String userName, String password, String userPhoto, String firstName, String lastName, String email) {

        ContentValues values = new ContentValues();
        values.put(UserConstants.USER_NAME, userName);
        values.put(UserConstants.PASSWORD, password);
        values.put(UserConstants.USER_PHOTO, userPhoto);
        values.put(UserConstants.FIRST_NAME, firstName);
        values.put(UserConstants.LAST_NAME, lastName);
        values.put(UserConstants.EMAIL, email);

        userID = database.insert(DBConstants.USER_TABLE, null, values);

        Cursor cursor = database.query(DBConstants.USER_TABLE, allColumnsUserTable, UserConstants.PK_USER_ID + " = " + userID, null, null, null, null);
        cursor.moveToFirst();
        User users = cursorToUser(cursor);
        cursor.close();

        return users;
    }

    public Event createEvent(String title, String description, String location, String photo, String color, String startDate, String endDate, String startTime, String endTime) {

        ContentValues values = new ContentValues();
        values.put(EventConstants.EVENT_TITLE, title);
        values.put(EventConstants.EVENT_DESCRIPTION, description);
        values.put(EventConstants.EVENT_LOCATION, location);
        values.put(EventConstants.EVENT_PHOTO, photo);
        values.put(EventConstants.EVENT_COLOR, color);
        values.put(EventConstants.EVENT_START_DATE, startDate);
        values.put(EventConstants.EVENT_END_DATE, endDate);
        values.put(EventConstants.EVENT_START_TIME, startTime);
        values.put(EventConstants.EVENT_END_TIME, endTime);

        eventID = database.insert(DBConstants.EVENT_TABLE, null, values);

        Cursor cursor = database.query(DBConstants.EVENT_TABLE, allColumnsEventTable, EventConstants.PK_EVENT_ID + " = " + eventID, null, null, null, null);
        cursor.moveToFirst();
        Event events = cursorToEvent(cursor);
        cursor.close();

        return events;
    }

    public UserEvent createUserEvent(long newUserID, long newEventID) {

        newUserID = userID;
        newEventID = eventID;

        ContentValues values = new ContentValues();
        values.put(UserEventConstants.FK_USER_ID, newUserID);
        values.put(UserEventConstants.FK_EVENT_ID, newEventID);

        long insertID = database.insert(DBConstants.USER_EVENT_TABLE, null, values);

        Cursor cursor = database.query(DBConstants.USER_EVENT_TABLE, allColumnsUserEventTable, UserEventConstants.PK_USER_EVENTS_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        UserEvent userEvents = cursorToUserEvent(cursor);
        cursor.close();

        return userEvents;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(DBConstants.USER_TABLE,
                allColumnsUserTable, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(DBConstants.EVENT_TABLE,
                allColumnsEventTable, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public List<UserEvent> getAllUserEvents() {
        List<UserEvent> userEvents = new ArrayList<UserEvent>();

        Cursor cursor = database.query(DBConstants.USER_EVENT_TABLE,
                allColumnsUserEventTable, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UserEvent userEvent = cursorToUserEvent(cursor);
            userEvents.add(userEvent);
            cursor.moveToNext();
        }
        cursor.close();
        return userEvents;
    }

    private User cursorToUser(Cursor cursor) {
        User users = new User();
        users.setUserID(cursor.getLong(0));
        users.setUserName(cursor.getString(1));
        users.setUserPassword(cursor.getString(2));
        users.setUserPhoto(cursor.getString(3));
        users.setUserFirstName(cursor.getString(4));
        users.setUserLastName(cursor.getString(5));
        users.setUserEmail(cursor.getString(6));
        return users;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event events = new Event();
        events.setEventID(cursor.getLong(0));
        events.setEventTitle(cursor.getString(1));
        events.setEventDescription(cursor.getString(2));
        events.setEventLocation(cursor.getString(3));
        events.setEventPhoto(cursor.getString(4));
        events.setEventColor(cursor.getString(5));
        events.setEventStartDate(cursor.getString(6));
        events.setEventEndDate(cursor.getString(7));
        events.setEventStartTime(cursor.getString(8));
        events.setEventEndTime(cursor.getString(9));
        return events;
    }

    private UserEvent cursorToUserEvent(Cursor cursor) {
        UserEvent userEvents = new UserEvent();
        userEvents.setUserEventID(cursor.getLong(0));
        userEvents.setFkUserID(cursor.getLong(1));
        userEvents.setFkEventID(cursor.getLong(2));
        return userEvents;
    }
}
