package com.mma.calendar.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mma.calendar.R;
import com.mma.calendar.activities.CalendarActivity;

public class CalendarAlarmService extends Service {

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent newIntent, int startId) {
        super.onStart(newIntent, startId);

        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), CalendarActivity.class);

        Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);

        mManager.notify(0, notification);
    }
}
