package com.mma.calendar.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.mma.calendar.R;
import com.mma.calendar.activities.NotificationDetails;
import com.mma.calendar.constants.Constants;

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
        String title = null;
        String description = null;
        String objectId = null;

        Bundle bundle = newIntent.getExtras();
        if (bundle != null) {
            title = bundle.getString(Constants.TITLE);
            description = bundle.getString(Constants.DESCRIPTION);
            objectId = bundle.getString(Constants.OBJECT_ID);
        }
        if (title == null) {
            title = "";
        }

        if (description == null) {
            description = "";
        }

        if (objectId == null) {
            objectId = "";
        }

        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), NotificationDetails.class);
        intent.putExtra(Constants.OBJECT_ID, objectId);

        Notification notification = new Notification(R.drawable.ic_launcher, description, System.currentTimeMillis());

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), title, description, pendingNotificationIntent);

        mManager.notify(0, notification);
    }
}
