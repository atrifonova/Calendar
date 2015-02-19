package com.mma.calendar.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CalendarReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, CalendarAlarmService.class);
        context.startService(service);
    }
}
