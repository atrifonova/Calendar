package com.mma.calendar.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CalendarReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getExtras().getString("title");
        String description = intent.getExtras().getString("description");
        Intent service = new Intent(context, CalendarAlarmService.class);
        service.putExtra("title", title);
        service.putExtra("description",description);
        context.startService(service);
    }
}
