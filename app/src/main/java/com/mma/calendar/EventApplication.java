package com.mma.calendar;

import android.app.Application;

import com.mma.calendar.model.Event;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Toni on 26-Feb-15.
 */
public class EventApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Event.class);

        Parse.initialize(this, "DCKoMDwof5x093rjDrebXmYKSz6jaUhX8pR7GZwO", "wItuxyAckUKIMTpmbHqYeRIbvLx9Zl25kTrO3GMF");
//        ParseAnalytics.trackAppOpened(getIntent());
    }
}
