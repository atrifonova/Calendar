package com.mma.calendar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mma.calendar.constants.DBConstants;
import com.mma.calendar.constants.EventConstants;
import com.mma.calendar.constants.UserConstants;
import com.mma.calendar.constants.UserEventConstants;

/**
 * Created by Toni on 03-Feb-15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + DBConstants.USER_TABLE + " ( "
            + UserConstants.PK_USER_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + UserConstants.USER_NAME + " TEXT NOT NULL, "
            + UserConstants.PASSWORD + " TEXT NOT NULL, "
            + UserConstants.USER_PHOTO + " BLOB, "
            + UserConstants.FIRST_NAME + " TEXT NOT NULL, "
            + UserConstants.LAST_NAME + " TEXT NOT NULL, "
            + UserConstants.EMAIL + " TEXT NOT NULL "
            + " ); ";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
            + DBConstants.EVENT_TABLE + " ( "
            + EventConstants.PK_EVENT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + EventConstants.EVENT_TITLE + " TEXT NOT NULL, "
            + EventConstants.EVENT_DESCRIPTION + " TEXT NOT NULL, "
            + EventConstants.EVENT_LOCATION + " TEXT, "
            + EventConstants.EVENT_PHOTO + " TEXT, "
            + EventConstants.EVENT_COLOR + " TEXT, "
            + EventConstants.EVENT_START_DATE + " DATE NOT NULL, "
            + EventConstants.EVENT_END_DATE + " DATE NOT NULL, "
            + EventConstants.EVENT_START_TIME + " TIME NOT NULL, "
            + EventConstants.EVENT_END_TIME + " TIME NOT NULL "
            + " ); ";

    private static final String CREATE_TABLE_USER_EVENT = "CREATE TABLE "
            + DBConstants.USER_EVENT_TABLE + " ( "
            + UserEventConstants.PK_USER_EVENTS_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + UserEventConstants.FK_USER_ID + " INTEGER NOT NULL, "
            + UserEventConstants.FK_EVENT_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY ( " + UserEventConstants.FK_USER_ID + " ) REFERENCES "
            + DBConstants.USER_TABLE + " ( " + UserConstants.PK_USER_ID + " ), "
            + "FOREIGN KEY ( " + UserEventConstants.FK_EVENT_ID + " ) REFERENCES "
            + DBConstants.EVENT_TABLE + " ( " + EventConstants.PK_EVENT_ID + " ) "
            + " ); ";


    public DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_USER_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("DB_UPGRADE", "Database upgrade from " + oldVersion + " " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.USER_TABLE);
        onCreate(db);
    }
}
