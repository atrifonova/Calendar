package com.mma.calendar.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mma.calendar.constants.Constants;

public class CalendarReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getExtras().getString(Constants.TITLE);
        String description = intent.getExtras().getString(Constants.DESCRIPTION);
        String objectID = intent.getExtras().getString(Constants.OBJECT_ID);
        Intent service = new Intent(context, CalendarAlarmService.class);
        service.putExtra(Constants.TITLE, title);
        service.putExtra(Constants.DESCRIPTION,description);
        service.putExtra(Constants.OBJECT_ID,objectID);
        context.startService(service);
    }
}
